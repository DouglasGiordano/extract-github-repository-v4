package br.edu.ufsm.requestpostgraphql.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;

/**
 *
 * @author Douglas Giordano
 */
@Data
@Entity
public class BranchCommit implements Serializable {
    
    @Id
    private String id;
    private String idBranch;
    private String idCommit;
    
    public BranchCommit(){
        
    }
    
    public BranchCommit(Branch branch, Commit commit){
        this.id = branch.getId() + commit.getId();
        this.idBranch = branch.getId();
        this.idCommit = commit.getId();
    }
}
