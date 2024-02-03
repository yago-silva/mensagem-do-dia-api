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

describe('Phrase e2e test', () => {
  const phrasePageUrl = '/phrase';
  const phrasePageUrlPattern = new RegExp('/phrase(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const phraseSample = {
    content: 'stimulating',
    createdAt: '2024-02-02T07:18:55.221Z',
    updatedAt: '2024-02-02T17:44:11.847Z',
    featured: false,
    active: false,
    slug: 'phew uh-huh flickering',
  };

  let phrase;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/phrases+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/phrases').as('postEntityRequest');
    cy.intercept('DELETE', '/api/phrases/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (phrase) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/phrases/${phrase.id}`,
      }).then(() => {
        phrase = undefined;
      });
    }
  });

  it('Phrases menu should load Phrases page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('phrase');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Phrase').should('exist');
    cy.url().should('match', phrasePageUrlPattern);
  });

  describe('Phrase page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(phrasePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Phrase page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/phrase/new$'));
        cy.getEntityCreateUpdateHeading('Phrase');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', phrasePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/phrases',
          body: phraseSample,
        }).then(({ body }) => {
          phrase = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/phrases+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [phrase],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(phrasePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Phrase page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('phrase');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', phrasePageUrlPattern);
      });

      it('edit button click should load edit Phrase page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Phrase');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', phrasePageUrlPattern);
      });

      it('edit button click should load edit Phrase page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Phrase');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', phrasePageUrlPattern);
      });

      it('last delete button click should delete instance of Phrase', () => {
        cy.intercept('GET', '/api/phrases/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('phrase').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', phrasePageUrlPattern);

        phrase = undefined;
      });
    });
  });

  describe('new Phrase page', () => {
    beforeEach(() => {
      cy.visit(`${phrasePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Phrase');
    });

    it('should create an instance of Phrase', () => {
      cy.get(`[data-cy="content"]`).type('over');
      cy.get(`[data-cy="content"]`).should('have.value', 'over');

      cy.get(`[data-cy="createdAt"]`).type('2024-02-02T04:37');
      cy.get(`[data-cy="createdAt"]`).blur();
      cy.get(`[data-cy="createdAt"]`).should('have.value', '2024-02-02T04:37');

      cy.get(`[data-cy="updatedAt"]`).type('2024-02-02T07:09');
      cy.get(`[data-cy="updatedAt"]`).blur();
      cy.get(`[data-cy="updatedAt"]`).should('have.value', '2024-02-02T07:09');

      cy.get(`[data-cy="featured"]`).should('not.be.checked');
      cy.get(`[data-cy="featured"]`).click();
      cy.get(`[data-cy="featured"]`).should('be.checked');

      cy.get(`[data-cy="active"]`).should('not.be.checked');
      cy.get(`[data-cy="active"]`).click();
      cy.get(`[data-cy="active"]`).should('be.checked');

      cy.get(`[data-cy="slug"]`).type('ah general bah');
      cy.get(`[data-cy="slug"]`).should('have.value', 'ah general bah');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        phrase = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', phrasePageUrlPattern);
    });
  });
});
