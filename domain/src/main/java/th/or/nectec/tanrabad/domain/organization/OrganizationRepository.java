package th.or.nectec.tanrabad.domain.organization;

import th.or.nectec.tanrabad.domain.WritableRepository;
import th.or.nectec.tanrabad.entity.Organization;

public interface OrganizationRepository extends WritableRepository<Organization> {
    Organization findById(int organizationId);
}
