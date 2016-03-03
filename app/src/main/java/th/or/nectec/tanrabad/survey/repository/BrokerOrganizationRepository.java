package th.or.nectec.tanrabad.survey.repository;

import java.util.List;

import th.or.nectec.tanrabad.domain.organization.OrganizationRepository;
import th.or.nectec.tanrabad.entity.Organization;
import th.or.nectec.tanrabad.survey.TanrabadApp;
import th.or.nectec.tanrabad.survey.repository.persistence.DbOrganizationRepository;

public class BrokerOrganizationRepository implements OrganizationRepository {

    private static BrokerOrganizationRepository instance;
    private OrganizationRepository cache;
    private OrganizationRepository database;

    public BrokerOrganizationRepository(OrganizationRepository cache, OrganizationRepository database) {
        this.cache = cache;
        this.database = database;
    }

    public static BrokerOrganizationRepository getInstance() {
        if (instance == null)
            instance = new BrokerOrganizationRepository(InMemoryOrganizationRepository.getInstance(),
                    new DbOrganizationRepository(TanrabadApp.getInstance()));
        return instance;
    }

    @Override
    public Organization findById(int organizationId) {
        Organization organization = cache.findById(organizationId);
        if (organization == null) {
            organization = database.findById(organizationId);
            cache.save(organization);
        }
        return organization;
    }

    @Override
    public boolean save(Organization organization) {
        boolean success = database.save(organization);
        if (success) {
            cache.save(organization);
        }
        return success;
    }

    @Override
    public boolean update(Organization organization) {
        boolean success = database.update(organization);
        if (success) {
            cache.update(organization);
        }
        return success;
    }

    @Override
    public void updateOrInsert(List<Organization> organizations) {

    }
}
