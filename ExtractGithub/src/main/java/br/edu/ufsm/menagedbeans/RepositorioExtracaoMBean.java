/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.menagedbeans;

import br.edu.ufsm.controller.ExtracaoCommit;
import br.edu.ufsm.controller.ExtracaoIssue;
import br.edu.ufsm.controller.ExtracaoPullRequest;
import br.edu.ufsm.controller.ExtracaoRepository;
import br.edu.ufsm.model.*;
import br.edu.ufsm.persistence.*;
import org.eclipse.egit.github.core.Comment;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.IssueService;
import uk.ac.wlv.sentistrength.SentiStrength;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.persistence.PersistenceException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Douglas Giordano
 */
@Named(value = "repositorioextracaobean")
@ViewScoped
public class RepositorioExtracaoMBean implements Serializable {

    private Project project;
    @EJB
    private ProjectDao projectDao;
    @EJB
    private CommitDao commitDao;
    @EJB
    private PullRequestDao pullRequestDao;
    @EJB
    private UsuarioAutenticacaoDao userDao;
    @EJB
    private GoogleGroupTopicDao googleGroupTopicDao;
    @EJB
    private IssueDao issueDao;
    private GitHubClient client;
    private List<UserAutenticacao> users;
    private UserAutenticacao user;
    private int progress;
    private String message;
    private int id;
    private Long countCommit;
    private Long countIssue;
    private Long countPullRequest;
    private Long countGroup;

    /**
     * Creates a new instance of ExtrairMBean
     */
    @PostConstruct
    public void init() {
        users = userDao.findAll(UserAutenticacao.class);
        if (!users.isEmpty()) {
            user = users.get(0);
        } else {
            user = new UserAutenticacao();
        }
        client = new GitHubClient();
        client.setCredentials(user.getUsuario(), user.getSenha());
    }

    public void load() {
        if (this.id != 0) {
            Long id = new Long(this.id);
            project = (Project) projectDao.getByIdO(id, Project.class);
        }
        this.countCommit = commitDao.count(project);
        this.countIssue = issueDao.count(project);
        this.countPullRequest = pullRequestDao.count(project);
        this.countGroup = googleGroupTopicDao.count(project);
    }

    public void extrairCommit() {
        try {
            this.message = "Starting extract of commit";
            atualizarProgresso(0, 100);
            RepositoryId repo = new RepositoryId(project.getOwner().getLogin(), project.getName());
            List<Commit> commits = ExtracaoCommit.extract(client, repo, project);
            int i = 0;
            int total = commits.size();
            for (Commit commit : commits) {
                i++;
                commitDao.merge(commit);
                this.message = "Extracting " + i + " of " + total;
                atualizarProgresso(i, total);
            }
            this.message = "Extraction Completed";
            Message.printMessageInfo("The extraction was successful.");
        } catch (Exception ex) {
            Message.printMessageError("An error has occurred: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void extrairFilesCommit() throws IOException {
        this.message = "Starting extract of commit file";
        atualizarProgresso(0, 100);
        RepositoryId repo = new RepositoryId(project.getOwner().getLogin(), project.getName());
        List<String> commits = commitDao.getCommits(project.getId());
        CommitService commitService = new CommitService(client);
        Commit commitD;
        RepositoryCommit commit;
        if (commits.isEmpty()) {
            Message.printMessageInfo("No commit found. Extract the commits before extracting the files.");
        }
        int i = 0;
        int total = commits.size();
        for (String id : commits) {
            i++;
            try {
                commit = commitService.getCommit(repo, id);
                commitD = new Commit(commit, project);
                this.message = "Extracting " + i + " of " + total;
                atualizarProgresso(i, total);
            } catch (Exception ex) {
                verificarCredenciais();
                extrairFilesCommit();
                break;
            }
            try {
                commitDao.merge(commitD);
            } catch (PersistenceException ex) {
                System.out.println(ex.getCause());
            } catch (Exception ex) {
                System.out.println(ex.getCause());
            }
        }
        this.message = "Extraction Completed";
        Message.printMessageInfo("The extraction was successful.");
    }

    public void extrairIssue() {
        try {
            this.message = "Starting extract of issues";
            atualizarProgresso(0, 100);
            RepositoryId repo = new RepositoryId(project.getOwner().getLogin(), project.getName());
            List<Issue> issues = ExtracaoIssue.extract(client, repo, project);
            this.message = "Starting persistence of issues";
            int i = 0;
            int total = issues.size();
            for (Issue issue : issues) {
                try {
                    i++;
                    this.message = "Extracting " + i + " of " + total;
                    issueDao.merge(issue);
                    atualizarProgresso(i, total);
                } catch (PersistenceException ex) {
                    System.out.println(ex.getMessage());
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
            this.message = "Extraction Completed";
            Message.printMessageInfo("The extraction was successful.");
        } catch (Exception ex) {
            Message.printMessageError("An error has occurred: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void extrairIssueComment() {
        this.message = "Starting extract of issues comment";
        atualizarProgresso(0, 100);
        RepositoryId repo = new RepositoryId(project.getOwner().getLogin(), project.getName());
        Project projeto = ExtracaoRepository.extractRepository(client, repo);
        List<Long> issues = issueDao.getIssues(projeto.getId());
        if (issues.isEmpty()) {
            Message.printMessageInfo("No issue found. Extract the issues before extracting the comments.");
            return;
        }
        IssueService serviceIssue = new IssueService(client);
        int i = 0;
        int total = issues.size();
        for (Long issueId : issues) {
            i++;
            Issue issue = (Issue) issueDao.getByIdO(issueId, Issue.class);
            if (issue.getComments() != 0) {
                List<Comment> comments = null;
                try {
                    comments = serviceIssue.getComments(repo, issue.getNumber());
                } catch (Exception ex) {
                    verificarCredenciais();
                    extrairIssueComment();
                    break;
                }

                List<IssueComment> issueComments = new ArrayList<>();
                for (Comment comment : comments) {
                    issueComments.add(new IssueComment(comment, issue));
                }
                issue.setIssueComments(issueComments);
                try {
                    this.message = "Extracting " + i + " of " + total;
                    issueDao.merge(issue);
                    atualizarProgresso(i, total);
                } catch (PersistenceException ex) {
                    System.out.println(ex.getCause());
                } catch (Exception ex) {
                    System.out.println(ex.getCause());
                }
            }
        }
        this.message = "Extraction Completed";
        Message.printMessageInfo("The extraction was successful.");
    }


    public void extrairPullRequest() {
        try {
            this.message = "Starting extract of pull request";
            atualizarProgresso(0, 100);
            RepositoryId repo = new RepositoryId(project.getOwner().getLogin(), project.getName());
            List<PullRequest> issues = ExtracaoPullRequest.extract(client, repo, project);
            int i = 0;
            int total = issues.size();
            for (PullRequest pull : issues) {
                i++;
                try {
                    this.message = "Extracting " + i + " of " + total;
                    atualizarProgresso(i, total);
                    pullRequestDao.merge(pull);
                } catch (PersistenceException ex) {
                    System.out.println(ex.getMessage());
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
            this.message = "Extraction Completed";
            Message.printMessageInfo("The extraction was successful.");
        } catch (Exception ex) {
            Message.printMessageError("An error has occurred: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void analisarSentimentos() {
        this.message = "Initiating feelings analysis";
        atualizarProgresso(0, 100);
        SentiStrength sentiStrength = new SentiStrength();
        String ssthInitialisation[] = {"sentidata", "/home/gpscom/data_2015/", "explain"};
        sentiStrength.initialise(ssthInitialisation);
        this.message = "Senti Strength started";
        List<Long> issues = issueDao.getIssuesId(project.getId());
        int i = 0;
        int total = issues.size();
        for (Long idIssue : issues) {
            i++;
            Issue issue = (Issue) issueDao.getByIdO(idIssue.longValue(), Issue.class);
            List<IssueComment> comentarios = issue.getIssueComments();
            this.message = "Analyzing " + i + " of " + total;
            atualizarProgresso(i, total);
            for (IssueComment comment : comentarios) {
                String resultado = sentiStrength.computeSentimentScores(comment.getBody()).substring(0, 4);
                getSentimento(comment.getSentimento(), resultado);
            }
            issueDao.merge(issue);
        }
        Message.printMessageInfo("The analysis was successful.");
    }

    public void analisarSentimentosIssue() {
        this.message = "Initiating feelings analysis";
        atualizarProgresso(0, 100);
        SentiStrength sentiStrength = new SentiStrength();
        String ssthInitialisation[] = {"sentidata", "/home/gpscom/data_2015/", "explain"};
        sentiStrength.initialise(ssthInitialisation);
        this.message = "Senti Strength started";
        List<Long> issues = issueDao.getIssuesAll(project.getId());
        int i = 0;
        int total = issues.size();
        for (Long idIssue : issues) {
            i++;
            this.message = "Analyzing " + i + " of " + total;
            atualizarProgresso(i, total);
            Issue issue = (Issue) issueDao.getByIdO(idIssue.longValue(), Issue.class);
            try {
                String resultado = sentiStrength.computeSentimentScores(issue.getBodyText()).substring(0, 4);
                getSentimento(issue.getSentimento(), resultado);
                issueDao.merge(issue);
            }catch (Exception ex){
                System.out.println(ex.getMessage());
            }
        }
        Message.printMessageInfo("The analysis was successful.");
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

    public void analisarSentimentosCommit() {
        this.message = "Initiating feelings analysis";
        atualizarProgresso(0, 100);
        SentiStrength sentiStrength = new SentiStrength();
        String ssthInitialisation[] = {"sentidata", "/home/gpscom/data_2015/", "explain"};
        sentiStrength.initialise(ssthInitialisation);
        this.message = "Senti Strength started";
        List<String> commits = commitDao.getCommitsId(project.getId());
        int i = 0;
        int total = commits.size();
        for (String idCommit : commits) {
            i++;
            this.message = "Analyzing " + i + " of " + total;
            atualizarProgresso(i, total);
            Commit commit = (Commit) commitDao.getByIdO(idCommit, Commit.class);
            String resultado = sentiStrength.computeSentimentScores(commit.getMessage()).substring(0, 4);
            getSentimento(commit.getSentimento(), resultado);
            commitDao.merge(commit);
        }
        Message.printMessageInfo("The analysis was successful.");
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
        users.remove(user);
        if (!users.isEmpty()) {
            user = users.get(0);
            client.setCredentials(user.getUsuario(), user.getSenha());
        } else {
            users = users = userDao.findAll(UserAutenticacao.class);
            user = users.get(0);
            client.setCredentials(user.getUsuario(), user.getSenha());
        }
    }

    private void atualizarProgresso(int i, int quantidade) {
        //calculo para o percentual do processo em relacao a quantidade de notas
        setProgress((i * 100) / quantidade);
    }

    /**
     * @return the project
     */
    public Project getProject() {
        return project;
    }

    /**
     * @param project the project to set
     */
    public void setProject(Project project) {
        this.project = project;
    }

    /**
     * @return the users
     */
    public List<UserAutenticacao> getUsers() {
        return users;
    }

    /**
     * @param users the users to set
     */
    public void setUsers(ArrayList<UserAutenticacao> users) {
        this.users = users;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getCountCommit() {
        return countCommit;
    }

    public void setCountCommit(Long countCommit) {
        this.countCommit = countCommit;
    }

    public Long getCountIssue() {
        return countIssue;
    }

    public void setCountIssue(Long countIssue) {
        this.countIssue = countIssue;
    }

    public Long getCountPullRequest() {
        return countPullRequest;
    }

    public void setCountPullRequest(Long countPullRequest) {
        this.countPullRequest = countPullRequest;
    }

    public Long getCountGroup() {
        return countGroup;
    }

    public void setCountGroup(Long countGroup) {
        this.countGroup = countGroup;
    }
}
