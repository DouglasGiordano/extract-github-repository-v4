/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.menagedbeans;

import br.edu.ufsm.model.*;
import br.edu.ufsm.persistence.GoogleGroupDao;
import br.edu.ufsm.persistence.GoogleGroupTopicDao;
import br.edu.ufsm.persistence.GoogleMessageDao;
import br.edu.ufsm.persistence.ProjectDao;
import org.primefaces.model.UploadedFile;
import uk.ac.wlv.sentistrength.SentiStrength;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author Douglas Giordano
 */
@Named(value = "googlegroupbean")
@ViewScoped
public class GoogleGroupMBean implements Serializable {
    @Inject
    private RepositorioExtracaoMBean repositorioMBean;
    private Project projeto;
    private GoogleGroup group;
    private List<GoogleGroup> groups;
    private UploadedFile uploadedFile;
    @EJB
    private GoogleGroupDao groupDao;
    @EJB
    private GoogleMessageDao messageDao;
    @EJB
    private GoogleGroupTopicDao topicDao;
    @EJB
    private ProjectDao projectDao;

    /**
     * Creates a new instance of ExtrairMBean
     */
    @PostConstruct
    public void init() {
        group = new GoogleGroup();
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
                .getRequest();

        String id = request.getParameter("id");
        projeto = projectDao.getById(Long.parseLong(id), Project.class);
        this.groups = groupDao.getGroups(projeto);
    }

    /**
     * @return the group
     */
    public GoogleGroup getGroup() {
        return group;
    }

    /**
     * @param group the group to set
     */
    public void setGroup(GoogleGroup group) {
        this.group = group;
    }

    public void upload() {
        this.projeto = repositorioMBean.getProject();
        System.out.println(this.projeto);
            File initialFile = new File("C:/Users/Dougl/OneDrive/Documentos/Mestrado/Java/LeitorCSVGoogleGroups/mailinglist-rails.csv");

            //group.setName("Elixir core");
            //group.setUrl("https://groups.google.com/forum/#!forum/elixir-lang-core");
        InputStream targetStream = null;
        try {
            targetStream = new FileInputStream(initialFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("iniciando");
            List<GoogleGroupMessage> messages = processInputFile(targetStream);
            System.out.println("mensagens importadas");
            System.out.println("salvando estrutura");
            group.setProject(getProjeto());
            groupDao.merge(group);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Group salvo com sucesso!", ""));
            int i = 0;
            for (GoogleGroupMessage message : messages) {

                try {
                    System.out.println("importando message: " + message.getId() + " topic: " + message.getTopic().getUrl());
                    messageDao.merge(message);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println(ex.getMessage());
                }
            }

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Mensagens e topicos salvos com sucesso!", ""));


    }

    private List<GoogleGroupMessage> processInputFile(InputStream inputFS) {
        List<GoogleGroupMessage> inputList = new LinkedList<GoogleGroupMessage>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
            inputList = br.lines().skip(1).map(mapToItem).collect(Collectors.toList());
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(GoogleGroupMBean.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GoogleGroupMBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return inputList;
    }

    /**
     * [0]usuario | [1]data | [2] mensagem| [3] url topic | [4]title | [5] id
     */
    private final Function<String, GoogleGroupMessage> mapToItem = (String line) -> {
        GoogleGroupMessage item = null;
        try {
            String[] p = line.trim().split("(\\s*(\\|)\\s*)");


            item = new GoogleGroupMessage();
            item.setId(p[5]);
            SimpleDateFormat simple = new SimpleDateFormat("dd/MM/yy");
            try {
                Date data = simple.parse(p[1]);
                item.setDate(data);
            } catch (ParseException ex) {
                Logger.getLogger(GoogleGroupMBean.class.getName()).log(Level.SEVERE, null, ex);
            }
            item.setMessage(p[2]);

            GoogleGroupUser user = new GoogleGroupUser();

            String userName = p[0].replace("  ", "").replace(".", "");
            if (p[0].equals("")) {
                user.setName("Usuario Sem Nome");
            } else {
                user.setName(userName.toLowerCase());//deixa primeira letra maiuscula
            }
            System.out.println("Analisando item " + p[5]);
            GoogleGroupTopic topic = new GoogleGroupTopic();
            topic.setTitle(p[4]);
            topic.setUrl(p[3]);
            topic.setGroup(this.group);

            item.setUser(user);
            item.setTopic(topic);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return item;
    };

    public void analisarSentimentos(GoogleGroup g) {
        SentiStrength sentiStrength = new SentiStrength();
        String ssthInitialisation[] = {"sentidata", "/home/gpscom/data_2015/", "explain"};
        sentiStrength.initialise(ssthInitialisation);
        List<GoogleGroupTopic> issues = messageDao.getGroupTopics(g);
        System.out.println(issues.size());
        try {
            for (GoogleGroupTopic topicId : issues) {
                GoogleGroupTopic topic = (GoogleGroupTopic) topicDao.getByIdO(topicId.getUrl(), GoogleGroupTopic.class);
                System.out.println("Analisando sentimentos topico: " + topic.getUrl());
                List<GoogleGroupMessage> messages = topic.getMessages();
                for (GoogleGroupMessage message : messages) {
                    String resultado = sentiStrength.computeSentimentScores(message.getMessage()).substring(0, 4);
                    getSentimento(message.getSentimento(), resultado);
                }
                topicDao.merge(topic);
            }
        } catch (Exception ex) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ocorreu um erro: " + ex.getMessage(), ""));
        }
    }

    public Sentimento getSentimento(Sentimento sentimento, String mensagem) {
        String[] msg = mensagem.split(" ");
        if (msg.length == 2) {
            int p = Integer.parseInt(msg[0]);
            int n = Integer.parseInt(msg[1]);
            int s = mySign(p + n);
            int sc = mySignC(p, n);
            System.out.println("    p: " + p
                    + " n: " + n
                    + " s: " + s
                    + " sc: " + sc
                    + "");
            sentimento.setN(n);
            sentimento.setP(p);
            sentimento.setS(s);
            sentimento.setSc(sc);
        }
        return sentimento;
    }

    public int mySign(int n) {
        if (n == 0) {
            return 0;
        }
        if (n > 0) {
            return 1;
        } else {
            return -1;
        }
    }

    public int mySignC(int p, int n) {
        if ((p > 3) && (n < -3)) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * @return the projeto
     */
    public Project getProjeto() {
        return projeto;
    }

    /**
     * @param projeto the projeto to set
     */
    public void setProjeto(Project projeto) {
        this.projeto = projeto;
    }

    /**
     * @return the groups
     */
    public List<GoogleGroup> getGroups() {
        return groups;
    }

    /**
     * @param groups the groups to set
     */
    public void setGroups(List<GoogleGroup> groups) {
        this.groups = groups;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }
}
