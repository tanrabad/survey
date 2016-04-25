package org.tanrabad.survey.domain.organization;

import org.tanrabad.survey.domain.WritableRepository;
import org.tanrabad.survey.entity.Organization;

public interface OrganizationRepository extends WritableRepository<Organization> {
    Organization findById(int organizationId);
}
