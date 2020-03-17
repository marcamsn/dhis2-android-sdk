CREATE TABLE OrganisationUnitGroup (_id INTEGER PRIMARY KEY AUTOINCREMENT, uid TEXT NOT NULL UNIQUE, code TEXT, name TEXT, displayName TEXT, created TEXT, lastUpdated TEXT, shortName TEXT, displayShortName TEXT);
CREATE TABLE OrganisationUnitOrganisationUnitGroupLink (_id INTEGER PRIMARY KEY AUTOINCREMENT, organisationUnit TEXT NOT NULL, organisationUnitGroup TEXT NOT NULL, FOREIGN KEY (organisationUnit) REFERENCES OrganisationUnit (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED, FOREIGN KEY (organisationUnitGroup) REFERENCES OrganisationUnitGroup (uid) ON DELETE CASCADE DEFERRABLE INITIALLY DEFERRED, UNIQUE (organisationUnit, organisationUnitGroup));