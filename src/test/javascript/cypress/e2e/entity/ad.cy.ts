import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Ad e2e test', () => {
  const adPageUrl = '/ad';
  const adPageUrlPattern = new RegExp('/ad(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const adSample = {
    createdAt: '2024-02-02T22:18:38.469Z',
    updatedAt: '2024-02-02T20:48:31.406Z',
    locale: 'nerve',
    deviceType: 'who than',
    featured: true,
    active: true,
  };

  let ad;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/ads+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/ads').as('postEntityRequest');
    cy.intercept('DELETE', '/api/ads/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (ad) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/ads/${ad.id}`,
      }).then(() => {
        ad = undefined;
      });
    }
  });

  it('Ads menu should load Ads page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('ad');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Ad').should('exist');
    cy.url().should('match', adPageUrlPattern);
  });

  describe('Ad page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(adPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Ad page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/ad/new$'));
        cy.getEntityCreateUpdateHeading('Ad');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', adPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/ads',
          body: adSample,
        }).then(({ body }) => {
          ad = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/ads+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [ad],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(adPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Ad page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('ad');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', adPageUrlPattern);
      });

      it('edit button click should load edit Ad page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Ad');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', adPageUrlPattern);
      });

      it('edit button click should load edit Ad page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Ad');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', adPageUrlPattern);
      });

      it('last delete button click should delete instance of Ad', () => {
        cy.intercept('GET', '/api/ads/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('ad').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', adPageUrlPattern);

        ad = undefined;
      });
    });
  });

  describe('new Ad page', () => {
    beforeEach(() => {
      cy.visit(`${adPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Ad');
    });

    it('should create an instance of Ad', () => {
      cy.get(`[data-cy="createdAt"]`).type('2024-02-02T15:32');
      cy.get(`[data-cy="createdAt"]`).blur();
      cy.get(`[data-cy="createdAt"]`).should('have.value', '2024-02-02T15:32');

      cy.get(`[data-cy="updatedAt"]`).type('2024-02-02T10:25');
      cy.get(`[data-cy="updatedAt"]`).blur();
      cy.get(`[data-cy="updatedAt"]`).should('have.value', '2024-02-02T10:25');

      cy.get(`[data-cy="locale"]`).type('without elastic');
      cy.get(`[data-cy="locale"]`).should('have.value', 'without elastic');

      cy.get(`[data-cy="deviceType"]`).type('oh');
      cy.get(`[data-cy="deviceType"]`).should('have.value', 'oh');

      cy.get(`[data-cy="featured"]`).should('not.be.checked');
      cy.get(`[data-cy="featured"]`).click();
      cy.get(`[data-cy="featured"]`).should('be.checked');

      cy.get(`[data-cy="active"]`).should('not.be.checked');
      cy.get(`[data-cy="active"]`).click();
      cy.get(`[data-cy="active"]`).should('be.checked');

      cy.get(`[data-cy="affiliateLink"]`).type('interferometer aggravating sans');
      cy.get(`[data-cy="affiliateLink"]`).should('have.value', 'interferometer aggravating sans');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        ad = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', adPageUrlPattern);
    });
  });
});
