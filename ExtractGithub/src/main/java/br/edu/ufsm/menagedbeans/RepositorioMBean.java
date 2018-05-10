/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ufsm.menagedbeans;

import br.edu.ufsm.controller.ExtracaoRepository;
import br.edu.ufsm.model.Project;
import br.edu.ufsm.persistence.ProjectDao;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.GitHubClient;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Douglas Giordano
 */
@Named(value = "repositoriobean")
@ViewScoped
public class RepositorioMBean implements Serializable {

    private List<Project> projects;
    @EJB
    private ProjectDao projectDao;
    private String repository;
    private String user;
    private GitHubClient client;

    /**
     * Creates a new instance of ExtrairMBean
     */
    @PostConstruct
    public void init() {
        client = new GitHubClient();
        client.setCredentials("douglasgiordano", "1995panquecas");
        projects = projectDao.findAll();
    }

    public void extrair() {
        System.out.println("Iniciando Extração");
        RepositoryId repo = new RepositoryId(user, repository);
        Project projeto = ExtracaoRepository.extractRepository(client, repo);
        System.out.println("Id projeto extraido"+projeto.getId());
        projectDao.save(projeto);
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Dados do repositório foram extraidos com sucesso.", ""));
        System.out.println("Extração Finalizada");
    }

    /**
     * @return the projects
     */
    public List<Project> getProjects() {
        return projects;
    }

    /**
     * @param projects the projects to set
     */
    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }

    /**
     * @return the repository
     */
    public String getRepository() {
        return repository;
    }

    /**
     * @param repository the repository to set
     */
    public void setRepository(String repository) {
        this.repository = repository;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }
}
