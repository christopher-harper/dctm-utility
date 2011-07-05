package fi.sardion.dctm.deploy.persistent;

import org.apache.tools.ant.Task;

public class Accessor extends Task {

    private String applicationPermit = "";
    private String extendedPermit = null;
    private String name;
    private String permit = String.valueOf(1);
    private int permitType = 0;

    public void setApplicationpermit(final String anApplicationPermit) {
	this.applicationPermit = anApplicationPermit;
    }

    public void setExtendedpermit(final String anExtendedPermit) {
	this.extendedPermit = anExtendedPermit;
    }

    public void setName(final String aName) {
	this.name = aName;
    }

    public void setPermit(final String aPermit) {
	this.permit = String.valueOf(Integer.parseInt(aPermit));
    }

    public void setPermittype(final String aPermitType) {
	this.permitType = Integer.parseInt(aPermitType);
    }

    protected String getApplicationPermit() {
	return this.applicationPermit;
    }

    /*-
     * Type Name:	dm_acl
    r_accessor_name                   CHAR(32)    REPEATING
    r_accessor_permit                 INTEGER     REPEATING
    r_accessor_xpermit                INTEGER     REPEATING
    r_is_group                        BOOLEAN     REPEATING
    globally_managed                  BOOLEAN   
    acl_class                         INTEGER   
    r_has_events                      BOOLEAN   
    r_permit_type                     INTEGER     REPEATING
    r_application_permit              CHAR(128)   REPEATING
    i_has_required_groups             BOOLEAN   
    i_has_required_group_set          BOOLEAN   
    i_has_access_restrictions         BOOLEAN   
    r_template_id                     ID        
    r_alias_set_id                    ID        
    i_partition                       INTEGER   
    i_is_replica                      BOOLEAN   
    i_vstamp                          INTEGER   

    USER ATTRIBUTES

    object_name                : dm_45012f5480000218
    description                : dm_45012f5480000218
    owner_name                 : dmadmin3
    globally_managed           : F
    acl_class                  : 0

    SYSTEM ATTRIBUTES

    r_object_id                : 45012f5480000218
    r_is_internal              : T
    r_accessor_name         [0]: dm_world
                          [1]: dm_owner
                          [2]: docu
    r_accessor_permit       [0]: 7
                          [1]: 7
                          [2]: 7
    r_accessor_xpermit      [0]: 0
                          [1]: 0
                          [2]: 3
    r_is_group              [0]: F
                          [1]: F
                          [2]: T
    r_has_events               : F
    r_permit_type           [0]: 0
                          [1]: 0
                          [2]: 0
    r_application_permit    [0]: 
                          [1]: 
                          [2]: 
    r_template_id              : 0000000000000000
    r_alias_set_id             : 0000000000000000

    APPLICATION ATTRIBUTES


    INTERNAL ATTRIBUTES

    i_has_required_groups      : F
    i_has_required_group_set   : F
    i_has_access_restrictions  : F
    i_partition                : 0
    i_is_replica               : F
    i_vstamp                   : 0

     *
     */
    protected String getExtendedPermit() {
	return this.extendedPermit;
    }

    protected String getName() {
	return this.name;
    }

    protected String getPermit() {
	return this.permit;
    }

    protected int getPermitType() {
	return this.permitType;
    }
}
/*-
 * $Log$
 */
