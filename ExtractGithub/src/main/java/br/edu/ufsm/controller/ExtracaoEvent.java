/*
 */
package br.edu.ufsm.controller;

import br.edu.ufsm.model.Issue;
import br.edu.ufsm.model.Project;
import br.edu.ufsm.model.event.Event;
import br.edu.ufsm.model.event.EventIssue;
import org.eclipse.egit.github.core.RepositoryId;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.IssueService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.eclipse.egit.github.core.IssueEvent;
import org.eclipse.egit.github.core.client.PageIterator;
import org.eclipse.egit.github.core.service.EventService;

/**
 *
 * @author Dougl
 */
public class ExtracaoEvent {

    public static List<EventIssue> extractIssueEvent(GitHubClient client, RepositoryId repositoryId, Project project) throws IOException {
        IssueService serviceIssue = new IssueService(client);
        PageIterator<IssueEvent> e = serviceIssue.pageEvents(repositoryId.getOwner(), repositoryId.getName());
        ArrayList<EventIssue> events = new ArrayList<>();
        while (e.hasNext()) {
            Collection<IssueEvent> eventC = e.next();
            for (IssueEvent event : eventC) {
                events.add(new EventIssue(event));
            }
        }
        return events;
    }

    public static List<Event> extractEvent(GitHubClient client, RepositoryId repositoryId, Project project) throws IOException {
        EventService serviceEvent = new EventService(client);
        PageIterator<org.eclipse.egit.github.core.event.Event> ei;
        ei = serviceEvent.pageEvents(repositoryId);
        ArrayList<Event> events = new ArrayList<>();
        while (ei.hasNext()) {
            Collection<org.eclipse.egit.github.core.event.Event> eventC = ei.next();
            for (org.eclipse.egit.github.core.event.Event event : eventC) {
                events.add(new Event(event));
            }
        }
        return events;
    }
}
