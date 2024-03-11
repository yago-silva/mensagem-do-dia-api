import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IAuthor } from 'app/shared/model/author.model';
import { getEntities as getAuthors } from 'app/entities/author/author.reducer';
import { ICategory } from 'app/shared/model/category.model';
import { getEntities as getCategories } from 'app/entities/category/category.reducer';
import { ITag } from 'app/shared/model/tag.model';
import { getEntities as getTags } from 'app/entities/tag/tag.reducer';
import { IPhrase } from 'app/shared/model/phrase.model';
import { getEntity, updateEntity, createEntity, reset } from './phrase.reducer';
import axios from 'axios';
import { Buffer } from 'buffer';
export const PhraseUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const authors = useAppSelector(state => state.author.entities);
  const categories = useAppSelector(state => state.category.entities);
  const tags = useAppSelector(state => state.tag.entities);
  const phraseEntity = useAppSelector(state => state.phrase.entity);
  const loading = useAppSelector(state => state.phrase.loading);
  const updating = useAppSelector(state => state.phrase.updating);
  const updateSuccess = useAppSelector(state => state.phrase.updateSuccess);

  const [mainMedia, setMainMedia] = useState('');

  const handleClose = () => {
    navigate('/phrase');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
    dispatch(getAuthors({}));
    dispatch(getCategories({}));
    dispatch(getTags({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.createdAt = convertDateTimeToServer(values.createdAt);
    values.updatedAt = convertDateTimeToServer(values.updatedAt);

    const entity = {
      ...phraseEntity,
      ...values,
      categories: mapIdList(values.categories),
      tags: mapIdList(values.tags),
      owner: users.find(it => it.id.toString() === values.owner.toString()),
      author: authors.find(it => it.id.toString() === values.author.toString()),
      mainMediaBase64: mainMedia ? mainMedia : null,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          createdAt: displayDefaultDateTime(),
          updatedAt: displayDefaultDateTime(),
        }
      : {
          ...phraseEntity,
          createdAt: convertDateTimeFromServer(phraseEntity.createdAt),
          updatedAt: convertDateTimeFromServer(phraseEntity.updatedAt),
          owner: phraseEntity?.owner?.id,
          author: phraseEntity?.author?.id,
          categories: phraseEntity?.categories?.map(e => e.id.toString()),
          tags: phraseEntity?.tags?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="mensagemDoDiaApp.phrase.home.createOrEditLabel" data-cy="PhraseCreateUpdateHeading">
            <Translate contentKey="mensagemDoDiaApp.phrase.home.createOrEditLabel">Create or edit a Phrase</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="phrase-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('mensagemDoDiaApp.phrase.content')}
                id="phrase-content"
                name="content"
                data-cy="content"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('mensagemDoDiaApp.phrase.createdAt')}
                id="phrase-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('mensagemDoDiaApp.phrase.updatedAt')}
                id="phrase-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('mensagemDoDiaApp.phrase.featured')}
                id="phrase-featured"
                name="featured"
                data-cy="featured"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('mensagemDoDiaApp.phrase.active')}
                id="phrase-active"
                name="active"
                data-cy="active"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('mensagemDoDiaApp.phrase.slug')}
                id="phrase-slug"
                name="slug"
                data-cy="slug"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="phrase-owner"
                name="owner"
                data-cy="owner"
                label={translate('mensagemDoDiaApp.phrase.owner')}
                type="select"
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="phrase-author"
                name="author"
                data-cy="author"
                label={translate('mensagemDoDiaApp.phrase.author')}
                type="select"
              >
                <option value="" key="0" />
                {authors
                  ? authors.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('mensagemDoDiaApp.phrase.category')}
                id="phrase-category"
                data-cy="category"
                type="select"
                multiple
                name="categories"
              >
                <option value="" key="0" />
                {categories
                  ? categories.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('mensagemDoDiaApp.phrase.tag')}
                id="phrase-tag"
                data-cy="tag"
                type="select"
                multiple
                name="tags"
              >
                <option value="" key="0" />
                {tags
                  ? tags.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              {mainMedia && (
                <div>
                  <img width="250px" src={'data:image/jpeg;base64,' + mainMedia.toString()} />
                </div>
              )}
              {!isNew && (
                <div>
                  <Button
                    color="primary"
                    id="save-entity"
                    type="button"
                    onClick={async () => {
                      axios
                        .get(`/api/media/phrase/${id}`, {
                          responseType: 'arraybuffer',
                        })
                        .then(response => {
                          var base64 = Buffer.from(response.data, 'binary').toString('base64');
                          setMainMedia(base64);
                        });
                    }}
                  >
                    Gerar imagem
                  </Button>
                </div>
              )}
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/phrase" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default PhraseUpdate;
