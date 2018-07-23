/*
 */
package br.edu.ufsm.menagedbeans;

import lombok.Data;

/**
 *
 * @author Douglas Giordano 01/07/2018
 */
@Data
public class CheckDownloadContent {
    private boolean event;
    private boolean eventIssue;
    private boolean commit;
    private boolean commitFiles;
    private boolean commitComment;
    private boolean issue;
    private boolean issueComment;
}
