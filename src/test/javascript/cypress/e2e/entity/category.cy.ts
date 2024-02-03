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

describe('Category e2e test', () => {
  const categoryPageUrl = '/category';
  const categoryPageUrlPattern = new RegExp('/category(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const categorySample = {
    name: 'expose',
    createdAt: '2024-02-02T03:49:28.229Z',
    updatedAt: '2024-02-03T00:15:00.365Z',
    featured: true,
    active: false,
    slug: 'unwitting tube',
  };

  let category;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/categories+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/categories').as('postEntityRequest');
    cy.intercept('DELETE', '/api/categories/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (category) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/categories/${category.id}`,
      }).then(() => {
        category = undefined;
      });
    }
  });

  it('Categories menu should load Categories page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('category');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Category').should('exist');
    cy.url().should('match', categoryPageUrlPattern);
  });

  describe('Category page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(categoryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Category page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/category/new$'));
        cy.getEntityCreateUpdateHeading('Category');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', categoryPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/categories',
          body: categorySample,
        }).then(({ body }) => {
          category = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/categories+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [category],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(categoryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Category page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('category');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', categoryPageUrlPattern);
      });

      it('edit button click should load edit Category page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Category');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', categoryPageUrlPattern);
      });

      it('edit button click should load edit Category page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Category');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', categoryPageUrlPattern);
      });

      it('last delete button click should delete instance of Category', () => {
        cy.intercept('GET', '/api/categories/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('category').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', categoryPageUrlPattern);

        category = undefined;
      });
    });
  });

  describe('new Category page', () => {
    beforeEach(() => {
      cy.visit(`${categoryPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Category');
    });

    it('should create an instance of Category', () => {
      cy.get(`[data-cy="name"]`).type('abaft partnership');
      cy.get(`[data-cy="name"]`).should('have.value', 'abaft partnership');

      cy.get(`[data-cy="createdAt"]`).type('2024-02-02T14:28');
      cy.get(`[data-cy="createdAt"]`).blur();
      cy.get(`[data-cy="createdAt"]`).should('have.value', '2024-02-02T14:28');

      cy.get(`[data-cy="updatedAt"]`).type('2024-02-02T11:28');
      cy.get(`[data-cy="updatedAt"]`).blur();
      cy.get(`[data-cy="updatedAt"]`).should('have.value', '2024-02-02T11:28');

      cy.get(`[data-cy="featured"]`).should('not.be.checked');
      cy.get(`[data-cy="featured"]`).click();
      cy.get(`[data-cy="featured"]`).should('be.checked');

      cy.get(`[data-cy="active"]`).should('not.be.checked');
      cy.get(`[data-cy="active"]`).click();
      cy.get(`[data-cy="active"]`).should('be.checked');

      cy.get(`[data-cy="slug"]`).type('hence hence blank');
      cy.get(`[data-cy="slug"]`).should('have.value', 'hence hence blank');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        category = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', categoryPageUrlPattern);
    });
  });
});
