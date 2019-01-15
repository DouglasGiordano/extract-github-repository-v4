package br.edu.ufsm.requestpostgraphql.service;

import br.edu.ufsm.requestpostgraphql.entity.Complex;
import br.edu.ufsm.requestpostgraphql.entity.Message;
import br.edu.ufsm.requestpostgraphql.entity.Repository;
import br.edu.ufsm.requestpostgraphql.entity.Status;
import br.edu.ufsm.requestpostgraphql.repository.StatusIssuePullRepository;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Douglas Giordano
 */
public class PullIssueService {

    private RequestService requestService = RequestService.getInstance();
    private IssueService issueService = new IssueService();
    private PullRequestService pullService = new PullRequestService();
    private StatusIssuePullRepository statusRepository = new StatusIssuePullRepository();

    public void extractPullIssue(Repository r) {
        Status status = this.getStatus(r);
        while ((!status.isCompleteIssue()) || (!status.isCompletePull())) {
            JSONObject jsonResponse = requestService.getHttpClient(getQuery(r, status));
            if (jsonResponse != null) {
                if (!status.isCompleteIssue()) {
                    System.out.println("(" + r.getOwner() + "/" + r.getName() + ") issue starting...");
                    try {
                        this.issueService.extract(jsonResponse, r, status);
                    } catch (JSONException ex) {
                        System.out.println("(" + r.getName() + "/ " + r.getOwner() + ") Error in extract JSON issues.");
                    }
                    Message.print(" (" + r.getOwner() + "/" + r.getName() + ") page issue complete...");
                }
                if (!status.isCompletePull()) {
                    System.out.println("(" + r.getOwner() + "/" + r.getName() + ") pull request starting...");
                    try {
                        this.pullService.extract(jsonResponse, r, status);
                    } catch (JSONException ex) {
                        System.out.println("(" + r.getName() + "/ " + r.getOwner() + ") Error in extract JSON pulls.");
                    }
                    Message.print(" (" + r.getOwner() + "/" + r.getName() + ") page pull request complete...");
                }
                status = this.statusRepository.save(status);
            }
            if (LoadManagement.getInstance().isBreak()) {
                Message.print("Break loading repository.");
                return;
            }
        }

    }

    public Status getStatus(Repository r) {
        Status status = this.statusRepository.find(r.getOwner(), r.getName());
        if (status != null) {
            return status;
        } else {
            return new Status(r.getOwner(), r.getName());
        }
    }

    public String getQuery(Repository repository, Status status) {
        List<Complex> fields = new ArrayList<>();
        if (!status.isCompleteIssue()) {
            Complex issue = this.issueService.getQuery(status.getEndCursorIssue());
            fields.add(issue);
        }
        if (!status.isCompletePull()) {
            Complex pullRequest = this.pullService.getQuery(status.getEndCursorPull());
            fields.add(pullRequest);
        }

        repository.setFields(fields);
        return "{\"query\": \"" + repository.toString() + "\"}";
    }

}
