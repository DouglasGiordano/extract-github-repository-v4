/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.menagedbeans;

import br.edu.ufsm.controller.ExtracaoIssue;
import br.edu.ufsm.controller.ExtracaoRepository;
import br.edu.ufsm.model.*;
import br.edu.ufsm.persistence.CountRegistersDao;
import br.edu.ufsm.persistence.IssueDao;
import br.edu.ufsm.persistence.ProjectDao;
import br.edu.ufsm.persistence.UsuarioAutenticacaoDao;
import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.client.RequestException;
import org.eclipse.egit.github.core.service.IssueService;
import org.primefaces.context.RequestContext;
import uk.ac.wlv.sentistrength.SentiStrength;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.PersistenceException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Douglas Giordano
 */
@Named(value = "extractrepositoriesmbean")
@ViewScoped
public class ExtractRepositoriesMBean implements Serializable {

    private String list;
    private List<Project> projects;
    private HashMap<Project, String> projectsStatus;
    @EJB
    private ProjectDao projectDao;
    @EJB
    private UsuarioAutenticacaoDao userDao;
    @EJB
    private IssueDao issueDao;
    private GitHubClient client;
    private List<UserAutenticacao> users;
    private UserAutenticacao user;
    private Project projectNow;
    private Project projectEnd;
    private int error;
    private static final Logger LOGGER = Logger.getLogger(ExtractRepositoriesMBean.class.getName());
    SentiStrength sentiStrength;

    /**
     * Creates a new instance of ExtrairMBean
     */
    @PostConstruct
    public void init() {
        setUsers(getUserDao().findAll(UserAutenticacao.class));
        if (!getUsers().isEmpty()) {
            setUser(getUsers().get(0));
        } else {
            setUser(new UserAutenticacao());
        }
        setClient(new GitHubClient());
        getClient().setCredentials(getUser().getUsuario(), getUser().getSenha());
        projects = new ArrayList<>();
        projectsStatus = new HashMap<>();
        sentiStrength = new SentiStrength();
        String ssthInitialisation[] = {"sentidata", "/home/gpscom/data_2015/", "explain"};

        sentiStrength.initialise(ssthInitialisation);
    }

    public void extractRepositories() {
        String newlist = list.replace("https://github.com/", "");
        String[] urlProjects = newlist.split("\\r?\\n");
        for (String url : urlProjects) {
            try {
                String[] info = url.split("/");
                String name = info[1];
                String owner = info[0];
                Project project = projectDao.getRepository(owner, name);
                if (project == null) {
                    RepositoryId repository = new RepositoryId(owner, name);
                    project = ExtracaoRepository.extractRepository(client, repository);
                    projectDao.save(project);
                }
                projects.add(project);
                projectsStatus.put(project, "Waiting");
            } catch (Exception ex) {
                printInfo("Error in url : " + url);
            }

        }
        this.setProjectNow(projects.get(0));
    }

    public void extract() {
        RequestContext context = RequestContext.getCurrentInstance();
        for (Project project : projects) {
            projectsStatus.put(project, "Starting");
            verificarCredenciais();
            this.setProjectNow(project);
            Long countIssue = issueDao.count(project);
            if (countIssue == null || countIssue == 0) {
                try {
                    extrairIssue(project);
                } catch (Exception ex) {
                    printError(project, ex);
                }
            }
            checkExtractIssueComment(project);
            try {
                analisarSentimentos(project);
                analisarSentimentosIssue(project);
            } catch (Exception ex) {
                printError(project, ex);
            }
            this.setProjectEnd(project);
            projectsStatus.put(project, "Completed");
        }
        this.setProjectNow(null);
        context.execute("PF('pool').stop()");
        Message.printMessageInfo("Extraction of repositories Completed");
    }

    public void checkExtractIssueComment(Project project) {
        try {
            extrairIssueComment(project);
        } catch (RequestException ex) {
            verificarCredenciais();
            printError(project, ex);
            checkExtractIssueComment(project);
        } catch (IOException ex) {
            verificarCredenciais();
            printError(project, ex);
            checkExtractIssueComment(project);
        } catch (Exception ex) {
            printError(project, ex);
        }
    }

    public void checkStatus() {
        RequestContext context = RequestContext.getCurrentInstance();
        if (getProjectNow() != null) {
            String id = "form:tbl:" + projects.indexOf(projectNow) + ":status";
            context.update(id);
        }
        if (getProjectEnd() != null) {
            String id = "form:tbl:" + projects.indexOf(getProjectEnd()) + ":status";
            context.update(id);
        }
    }

    public void extrairIssue(Project projeto) {
        projectsStatus.put(projeto, "Starting extract of issues");
        RepositoryId repo = new RepositoryId(projeto.getOwner().getLogin(), projeto.getName());
        List<Issue> issues = null;
        try {
            issues = ExtracaoIssue.extract(getClient(), repo, projeto);
        } catch (Exception ex) {
            verificarCredenciais();
            printError(projeto, ex);
            try {
                issues = ExtracaoIssue.extract(getClient(), repo, projeto);
            } catch (IOException e) {
                printError(projeto, ex);
            }
        }
        projectsStatus.put(projeto, "Starting persistence of issues");
        int i = 0;
        int total = issues.size();
        for (Issue issue : issues) {
            try {
                i++;
                projectsStatus.put(projeto, "Extracting issue" + i + " of " + total);
                getIssueDao().merge(issue);
            } catch (PersistenceException ex) {
                projectsStatus.put(projeto, "Erro: " + ex.getMessage());
                printError(projeto, ex);
            } catch (Exception ex) {
                projectsStatus.put(projeto, "Erro: " + ex.getMessage());
                printError(projeto, ex);
            }
        }
        projectsStatus.put(projeto, "Extraction issue Completed");
    }

    public Project extrairIssueComment(Project projeto) throws IOException {
        projectsStatus.put(projeto, "Starting extract of issues comment");
        RepositoryId repo = new RepositoryId(projeto.getOwner().getLogin(), projeto.getName());
        List<Long> issues = getIssueDao().getIssues(projeto.getId());
        if (issues.isEmpty()) {
            projectsStatus.put(projeto, "No issue found. Extract the issues before extracting the comments.");
            return projeto;
        }
        IssueService serviceIssue = new IssueService(getClient());
        int i = 0;
        int total = issues.size();
        for (Long issueId : issues) {
            i++;
            Issue issue = (Issue) getIssueDao().getByIdO(issueId, Issue.class);
            if (issue.getComments() != 0) {
                List<Comment> comments = null;
                comments = serviceIssue.getComments(repo, issue.getNumber());

                List<IssueComment> issueComments = new ArrayList<>();
                for (Comment comment : comments) {
                    issueComments.add(new IssueComment(comment, issue));
                }
                issue.setIssueComments(issueComments);
                try {
                    projectsStatus.put(projeto, "Extracting comment" + i + " of " + total);
                    getIssueDao().merge(issue);
                } catch (PersistenceException ex) {
                    projectsStatus.put(projeto, "Error: " + ex.getMessage());
                    printError(projeto, ex);
                } catch (Exception ex) {
                    projectsStatus.put(projeto, "Error: " + ex.getMessage());
                    printError(projeto, ex);
                }
            }
        }
        projectsStatus.put(projeto, "Extraction issue comment Completed");
        return projeto;
    }

    private void printError(Project project, Exception ex) {
        String mensagem = "Error " + error + " in project " + project.getId() + " " + project.getName() + " " + project.getUrl();
        LOGGER.log(Level.WARNING, mensagem, ex);
    }

    private void printInfo(String mensagem) {
        LOGGER.log(Level.INFO, mensagem);
    }

    public void analisarSentimentos(Project projeto) {
        projectsStatus.put(projeto, "Initiating issue comment feelings analysis");
        projectsStatus.put(projeto, "Senti Strength started");
        List<Long> issues = getIssueDao().getIssuesId(projeto.getId());
        int i = 0;
        int total = issues.size();
        for (Long idIssue : issues) {
            i++;
            Issue issue = (Issue) getIssueDao().getByIdO(idIssue.longValue(), Issue.class);
            List<IssueComment> comentarios = issue.getIssueComments();
            projectsStatus.put(projeto, "Analyzing comment" + i + " of " + total);
            for (IssueComment comment : comentarios) {
                String resultado = sentiStrength.computeSentimentScores(comment.getBody()).substring(0, 4);
                getSentimento(comment.getSentimento(), resultado);
            }
            getIssueDao().merge(issue);
        }
        projectsStatus.put(projeto, "The analysis issue comment was successful.");
    }

    public void analisarSentimentosIssue(Project projeto) {
        projectsStatus.put(projeto, "Initiating issue feelings analysis");
        SentiStrength sentiStrength = new SentiStrength();
        String ssthInitialisation[] = {"sentidata", "/home/gpscom/data_2015/", "explain"};
        sentiStrength.initialise(ssthInitialisation);
        projectsStatus.put(projeto, "Senti Strength started");
        List<Long> issues = getIssueDao().getIssuesAll(projeto.getId());
        int i = 0;
        int total = issues.size();
        for (Long idIssue : issues) {
            i++;
            projectsStatus.put(projeto, "Analyzing issue" + i + " of " + total);
            Issue issue = (Issue) getIssueDao().getByIdO(idIssue.longValue(), Issue.class);
            try {
                String resultado = sentiStrength.computeSentimentScores(issue.getTitle() + " " + issue.getBodyText()).substring(0, 4);
                getSentimento(issue.getSentimento(), resultado);
                getIssueDao().merge(issue);
            } catch (Exception ex) {
                projectsStatus.put(projeto, "Error: " + ex.getMessage());
                printError(projeto, ex);
            }
        }
        projectsStatus.put(projeto, "The analysis issue was successful.");
    }

    public String getMessageStatus(Project p) {
        return projectsStatus.get(p);
    }

    public Sentimento getSentimento(Sentimento sentimento, String mensagem) {
        String[] msg = mensagem.split(" ");
        if (msg.length == 2) {
            int p = Integer.parseInt(msg[0]);
            int n = Integer.parseInt(msg[1]);
            int s = mySign(p + n);
            int sc = mySignC(p, n);
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

    public void verificarCredenciais() {
        getUsers().remove(getUser());
        if (!getUsers().isEmpty()) {
            setUser(getUsers().get(0));
            getClient().setCredentials(getUser().getUsuario(), getUser().getSenha());
        } else {
            this.users = getUserDao().findAll(UserAutenticacao.class);
            setUser(getUsers().get(0));
            getClient().setCredentials(getUser().getUsuario(), getUser().getSenha());
        }
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    public ProjectDao getProjectDao() {
        return projectDao;
    }

    public void setProjectDao(ProjectDao projectDao) {
        this.projectDao = projectDao;
    }

    public UsuarioAutenticacaoDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UsuarioAutenticacaoDao userDao) {
        this.userDao = userDao;
    }

    public IssueDao getIssueDao() {
        return issueDao;
    }

    public void setIssueDao(IssueDao issueDao) {
        this.issueDao = issueDao;
    }

    public GitHubClient getClient() {
        return client;
    }

    public void setClient(GitHubClient client) {
        this.client = client;
    }

    public List<UserAutenticacao> getUsers() {
        return users;
    }

    public void setUsers(List<UserAutenticacao> users) {
        this.users = users;
    }

    public UserAutenticacao getUser() {
        return user;
    }

    public void setUser(UserAutenticacao user) {
        this.user = user;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public Project getProjectNow() {
        return projectNow;
    }

    public void setProjectNow(Project projectNow) {
        this.projectNow = projectNow;
    }

    public Project getProjectEnd() {
        return projectEnd;
    }

    public void setProjectEnd(Project projectEnd) {
        this.projectEnd = projectEnd;
    }

    public HashMap<Project, String> getProjectsStatus() {
        return projectsStatus;
    }

    public void setProjectsStatus(HashMap<Project, String> projectsStatus) {
        this.projectsStatus = projectsStatus;
    }
}
