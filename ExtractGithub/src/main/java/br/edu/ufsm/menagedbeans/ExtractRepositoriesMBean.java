/*
 */
package br.edu.ufsm.menagedbeans;

import br.edu.ufsm.controller.ExtracaoCommit;
import br.edu.ufsm.controller.ExtracaoEvent;
import br.edu.ufsm.controller.ExtracaoIssue;
import br.edu.ufsm.controller.ExtracaoRepository;
import br.edu.ufsm.model.*;
import br.edu.ufsm.model.event.Event;
import br.edu.ufsm.model.event.EventIssue;
import br.edu.ufsm.persistence.CommitCommentDao;
import br.edu.ufsm.persistence.CommitDao;
import br.edu.ufsm.persistence.EventDao;
import br.edu.ufsm.persistence.EventIssueDao;
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
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.service.CommitService;

/**
 * @author Douglas Giordano
 */
@Named(value = "extractrepositoriesmbean")
@ViewScoped
public class ExtractRepositoriesMBean implements Serializable {

    @EJB
    private ProjectDao projectDao;
    @EJB
    private UsuarioAutenticacaoDao userDao;
    @EJB
    private IssueDao issueDao;
    @EJB
    private EventDao eventDao;
    @EJB
    private EventIssueDao eventIssueDao;
    @EJB
    private CommitDao commitDao;
    @EJB
    private CommitCommentDao commitCommentDao;

    private String list;
    private List<Project> projects;
    private HashMap<Project, String> projectsStatus;
    private GitHubClient client;
    private List<UserAutenticacao> users;
    private UserAutenticacao user;
    private Project projectNow;
    private Project projectEnd;
    private static final Logger LOGGER = Logger.getLogger(ExtractRepositoriesMBean.class.getName());
    private SentiStrength sentiStrength;
    private CheckDownloadContent checkDownloadContent;
    private CheckAnalysisContent checkAnalysisContent;

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
        checkDownloadContent = new CheckDownloadContent();
        checkAnalysisContent = new CheckAnalysisContent();
        ClassLoader classLoader = getClass().getClassLoader();
        String localizacaoSchemas = classLoader.getResource("sentistrength/data_2015").getPath();
        sentiStrength = new SentiStrength();
        String ssthInitialisation[] = {"sentidata", localizacaoSchemas, "explain"};

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
            checkExtractIssue(project);
            checkExtractIssueComment(project);
            checkExtractEvent(project);
            checkExtractEventIssue(project);
            checkExtractCommit(project);
            checkExtractCommitFile(project);
            checkExtractCommitComment(project);
            checkCommitfeeling(project);
            checkIssuefeeling(project);
            this.setProjectEnd(project);
            projectsStatus.put(project, "Completed");
        }
        this.setProjectNow(null);
        try {
            Thread.sleep(30000);
        } catch (InterruptedException ex) {
            Logger.getLogger(ExtractRepositoriesMBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        context.execute("PF('pool').stop()");
        Message.printMessageInfo("Extraction of repositories Completed");
    }

    public void checkExtractIssueComment(Project project) {
        if (checkDownloadContent.isIssueComment()) {
            try {
                extrairIssueComment(project);
            } catch (RequestException ex) {
                verificarCredenciais();
                printError(project, ex);
                checkExtractIssueComment(project);
            } catch (IOException ex) {
                printError(project, ex);
            }
        }
    }

    public void checkExtractEvent(Project project) {
        if (checkDownloadContent.isEvent()) {
            try {
                extrairEvents(project);
            } catch (RequestException ex) {
                verificarCredenciais();
                printError(project, ex);
                checkExtractEvent(project);
            } catch (IOException ex) {
                printError(project, ex);
            }
        }
    }

    public void checkExtractEventIssue(Project project) {
        if (checkDownloadContent.isEventIssue()) {
            try {
                extrairIssueEvents(project);
            } catch (RequestException ex) {
                verificarCredenciais();
                printError(project, ex);
                checkExtractEventIssue(project);
            } catch (IOException ex) {
                printError(project, ex);
            }
        }

    }

    public void checkExtractIssue(Project project) {
        if (checkDownloadContent.isIssue()) {
            try {
                extrairIssue(project);
            } catch (RequestException ex) {
                verificarCredenciais();
                printError(project, ex);
                checkExtractIssue(project);
            } catch (IOException ex) {
                printError(project, ex);
            }
        }
    }

    public void checkExtractCommit(Project project) {
        if (checkDownloadContent.isCommit()) {
            try {
                extrairCommit(project);
            } catch (RequestException ex) {
                verificarCredenciais();
                printError(project, ex);
                checkExtractCommit(project);
            } catch (IOException ex) {
                printError(project, ex);
            }
        }

    }

    public void checkExtractCommitFile(Project project) {
        if (checkDownloadContent.isCommitFiles()) {
            try {
                extrairFilesCommit(project);
            } catch (RequestException ex) {
                verificarCredenciais();
                printError(project, ex);
                checkExtractCommitFile(project);
            } catch (IOException ex) {
                printError(project, ex);
            }
        }
    }

    public void checkExtractCommitComment(Project project) {
        if (checkDownloadContent.isCommitComment()) {
            try {
                extrairCommentCommit(project);
            } catch (RequestException ex) {
                verificarCredenciais();
                printError(project, ex);
                checkExtractCommitFile(project);
            } catch (IOException ex) {
                printError(project, ex);
            }
        }
    }

    public void checkCommitfeeling(Project project) {
        if (checkAnalysisContent.isCommitFeeling()) {
            analysisFeelingCommit(project);
        }
    }

    public void checkIssuefeeling(Project project) {
        if (checkAnalysisContent.isIssueFeeling()) {
            analysisFeelingIssue(project);
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

    public void extrairIssue(Project projeto) throws IOException {
        projectsStatus.put(projeto, "Starting extract of issues");
        RepositoryId repo = new RepositoryId(projeto.getOwner().getLogin(), projeto.getName());
        List<Issue> issues = null;
        issues = ExtracaoIssue.extract(getClient(), repo, projeto);
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

    public void extrairEvents(Project projeto) throws IOException {
        projectsStatus.put(projeto, "Starting extract of events");
        RepositoryId repo = new RepositoryId(projeto.getOwner().getLogin(), projeto.getName());
        List<Event> events = null;
        events = ExtracaoEvent.extractEvent(getClient(), repo, projeto);
        projectsStatus.put(projeto, "Starting persistence of events");
        int i = 0;
        int total = events.size();
        for (Event event : events) {
            try {
                i++;
                projectsStatus.put(projeto, "Extracting event" + i + " of " + total);
                eventDao.save(event);
            } catch (PersistenceException ex) {
                projectsStatus.put(projeto, "Erro: " + ex.getMessage());
                printError(projeto, ex);
            } catch (Exception ex) {
                projectsStatus.put(projeto, "Erro: " + ex.getMessage());
                printError(projeto, ex);
            }
        }
        projectsStatus.put(projeto, "Extraction event Completed");
    }

    public void extrairIssueEvents(Project projeto) throws IOException {
        projectsStatus.put(projeto, "Starting extract of events issue");
        RepositoryId repo = new RepositoryId(projeto.getOwner().getLogin(), projeto.getName());
        List<EventIssue> events = null;
        events = ExtracaoEvent.extractIssueEvent(getClient(), repo, projeto);
        projectsStatus.put(projeto, "Starting persistence of events issue");
        int i = 0;
        int total = events.size();
        for (EventIssue event : events) {
            try {
                i++;
                projectsStatus.put(projeto, "Extracting issue event" + i + " of " + total);
                eventIssueDao.save(event);
            } catch (PersistenceException ex) {
                projectsStatus.put(projeto, "Erro: " + ex.getMessage());
                printError(projeto, ex);
            } catch (Exception ex) {
                projectsStatus.put(projeto, "Erro: " + ex.getMessage());
                printError(projeto, ex);
            }
        }
        projectsStatus.put(projeto, "Extraction issue event Completed");
    }

    public void extrairCommit(Project projeto) throws IOException {
        try {
            projectsStatus.put(projeto, "Starting extract of commit");
            RepositoryId repo = new RepositoryId(projeto.getOwner().getLogin(), projeto.getName());
            List<Commit> commits = ExtracaoCommit.extract(client, repo, projeto);
            int total = commits.size();
            int i = 0;
            for (Commit commit : commits) {
                i++;
                commitDao.merge(commit);
                projectsStatus.put(projeto, "Extracting " + i + " of " + total);
            }
            Message.printMessageInfo("The extraction was successful.");
        } catch (Exception ex) {
            projectsStatus.put(projeto, "An error has occurred: " + ex.getMessage());
            printError(projeto, ex);
        }
    }

    public void extrairFilesCommit(Project projeto) throws IOException {
        projectsStatus.put(projeto, "Starting extract of commit file");
        RepositoryId repo = new RepositoryId(projeto.getOwner().getLogin(), projeto.getName());
        List<String> commits = commitDao.getCommitsNotFile(projeto.getId());
        CommitService commitService = new CommitService(client);
        Commit commitD;
        RepositoryCommit commit;
        int i = 0;
        int total = commits.size();
        for (String id : commits) {
            i++;
            commit = commitService.getCommit(repo, id);
            commitD = new Commit(commit, projeto);
            projectsStatus.put(projeto, "Extracting " + i + " of " + total);
            try {
                commitDao.merge(commitD);
            } catch (PersistenceException ex) {
                projectsStatus.put(projeto, "Erro: " + ex.getMessage());
                printError(projeto, ex);
            } catch (Exception ex) {
                projectsStatus.put(projeto, "Erro: " + ex.getMessage());
                printError(projeto, ex);
            }
        }
        projectsStatus.put(projeto, "Extraction Completed commit file");
        projectsStatus.put(projeto, "The extraction was successful commit file");
    }

    public void extrairCommentCommit(Project projeto) throws IOException {
        projectsStatus.put(projeto, "Starting extract of commit comment");
        RepositoryId repo = new RepositoryId(projeto.getOwner().getLogin(), projeto.getName());
        List<String> commits = commitDao.getCommitsNotComment(projeto.getId());
        CommitService commitService = new CommitService(client);
        int i = 0;
        int total = commits.size();
        for (String id : commits) {
            i++;
            List<org.eclipse.egit.github.core.CommitComment> comments = null;
            comments = commitService.getComments(repo, id);
            comments.stream().map((comentario) -> {
                Commit commit = commitDao.getById(id, Commit.class);
                br.edu.ufsm.model.CommitComment c = new br.edu.ufsm.model.CommitComment(comentario, commit);
                return c;
            }).forEachOrdered((c) -> {
                try {
                    commitCommentDao.save(c);
                } catch (PersistenceException ex) {
                    projectsStatus.put(projeto, "Erro: " + ex.getMessage());
                    printError(projeto, ex);
                } catch (Exception ex) {
                    projectsStatus.put(projeto, "Erro: " + ex.getMessage());
                    printError(projeto, ex);
                }

            });
            projectsStatus.put(projeto, "Extracting " + i + " of " + total);
        }
        projectsStatus.put(projeto, "Extraction Completed commit comment");
        projectsStatus.put(projeto, "The extraction was successful commit comment");
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
        String mensagem = "Error " + " in project " + project.getId() + " " + project.getName() + " " + project.getUrl();
        mensagem += "\nProgress: " + projectsStatus.get(project);
        LOGGER.log(Level.WARNING, mensagem, ex.getMessage());
    }

    private void printInfo(String mensagem) {
        LOGGER.log(Level.INFO, mensagem);
    }

    public void analysisFeelingIssue(Project projeto) {
        projectsStatus.put(projeto, "Initiating issue comment feelings analysis");
        projectsStatus.put(projeto, "Senti Strength started");
        List<Long> issues = getIssueDao().getIssuesId(projeto.getId());
        int i = 0;
        int total = issues.size();
        for (Long idIssue : issues) {
            i++;
            Issue issue = (Issue) getIssueDao().getByIdO(idIssue.longValue(), Issue.class);
            List<IssueComment> comentarios = issue.getIssueComments();
            projectsStatus.put(projeto, "Analyzing issue " + i + " of " + total);
            for (IssueComment comment : comentarios) {
                String resultado = this.sentiStrength.computeSentimentScores(comment.getBody()).substring(0, 4);
                getSentimento(comment.getSentimento(), resultado);
            }
            String resultado = this.sentiStrength.computeSentimentScores(issue.getTitle() + " " + issue.getBodyText()).substring(0, 4);
            getSentimento(issue.getSentimento(), resultado);
            getIssueDao().merge(issue);
        }
        projectsStatus.put(projeto, "The analysis issue comment was successful.");
    }

    public void analysisFeelingCommit(Project projeto) {
        projectsStatus.put(projeto, "Initiating commit feelings analysis");
        projectsStatus.put(projeto, "Senti Strength started");
        List<String> issues = commitDao.getCommitsId(projeto.getId());
        int i = 0;
        int total = issues.size();
        for (String id : issues) {
            i++;
            projectsStatus.put(projeto, "Analyzing commit " + i + " of " + total);

            try {
                Commit commit = (Commit) commitDao.getByIdO(id, Commit.class);
                String resultado = this.sentiStrength.computeSentimentScores(commit.getMessage()).substring(0, 4);
                getSentimento(commit.getSentimento(), resultado);
                commitDao.merge(commit);
                List<CommitComment> comentarios = commitCommentDao.getComments(commit);
                for (CommitComment comment : comentarios) {
                    String resultadoC = this.sentiStrength.computeSentimentScores(comment.getBodyText()).substring(0, 4);
                    getSentimento(comment.getSentimento(), resultadoC);
                    commitCommentDao.save(comment);
                }
            } catch (Exception ex) {
                projectsStatus.put(projeto, "Error: " + ex.getMessage());
                printError(projeto, ex);
            }
        }
        projectsStatus.put(projeto, "The analysis commit was successful.");
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

    /**
     * @return the checkDownloadContent
     */
    public CheckDownloadContent getCheckDownloadContent() {
        return checkDownloadContent;
    }

    /**
     * @param checkDownloadContent the checkDownloadContent to set
     */
    public void setCheckDownloadContent(CheckDownloadContent checkDownloadContent) {
        this.checkDownloadContent = checkDownloadContent;
    }

    /**
     * @return the checkAnalysisContent
     */
    public CheckAnalysisContent getCheckAnalysisContent() {
        return checkAnalysisContent;
    }

    /**
     * @param checkAnalysisContent the checkAnalysisContent to set
     */
    public void setCheckAnalysisContent(CheckAnalysisContent checkAnalysisContent) {
        this.checkAnalysisContent = checkAnalysisContent;
    }
}
