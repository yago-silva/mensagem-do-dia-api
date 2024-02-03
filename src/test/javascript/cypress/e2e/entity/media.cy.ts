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

describe('Media e2e test', () => {
  const mediaPageUrl = '/media';
  const mediaPageUrlPattern = new RegExp('/media(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const mediaSample = {
    createdAt: '2024-02-02T19:14:17.934Z',
    updatedAt: '2024-02-02T17:40:52.205Z',
    type: 'IMAGE',
    url: 'https://drafty-acid.info/',
    width: 1621,
    height: 885,
    active: false,
  };

  let media;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/media+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/media').as('postEntityRequest');
    cy.intercept('DELETE', '/api/media/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (media) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/media/${media.id}`,
      }).then(() => {
        media = undefined;
      });
    }
  });

  it('Media menu should load Media page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('media');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Media').should('exist');
    cy.url().should('match', mediaPageUrlPattern);
  });

  describe('Media page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(mediaPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Media page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/media/new$'));
        cy.getEntityCreateUpdateHeading('Media');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', mediaPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/media',
          body: mediaSample,
        }).then(({ body }) => {
          media = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/media+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [media],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(mediaPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Media page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('media');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', mediaPageUrlPattern);
      });

      it('edit button click should load edit Media page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Media');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', mediaPageUrlPattern);
      });

      it('edit button click should load edit Media page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Media');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', mediaPageUrlPattern);
      });

      it('last delete button click should delete instance of Media', () => {
        cy.intercept('GET', '/api/media/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('media').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', mediaPageUrlPattern);

        media = undefined;
      });
    });
  });

  describe('new Media page', () => {
    beforeEach(() => {
      cy.visit(`${mediaPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Media');
    });

    it('should create an instance of Media', () => {
      cy.get(`[data-cy="createdAt"]`).type('2024-02-02T08:39');
      cy.get(`[data-cy="createdAt"]`).blur();
      cy.get(`[data-cy="createdAt"]`).should('have.value', '2024-02-02T08:39');

      cy.get(`[data-cy="updatedAt"]`).type('2024-02-02T07:45');
      cy.get(`[data-cy="updatedAt"]`).blur();
      cy.get(`[data-cy="updatedAt"]`).should('have.value', '2024-02-02T07:45');

      cy.get(`[data-cy="type"]`).select('VIDEO');

      cy.get(`[data-cy="url"]`).type('https://wooden-sidecar.com/');
      cy.get(`[data-cy="url"]`).should('have.value', 'https://wooden-sidecar.com/');

      cy.get(`[data-cy="width"]`).type('30184');
      cy.get(`[data-cy="width"]`).should('have.value', '30184');

      cy.get(`[data-cy="height"]`).type('15627');
      cy.get(`[data-cy="height"]`).should('have.value', '15627');

      cy.get(`[data-cy="active"]`).should('not.be.checked');
      cy.get(`[data-cy="active"]`).click();
      cy.get(`[data-cy="active"]`).should('be.checked');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        media = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', mediaPageUrlPattern);
    });
  });
});
