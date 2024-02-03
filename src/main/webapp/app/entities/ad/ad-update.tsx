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
import { ICategory } from 'app/shared/model/category.model';
import { getEntities as getCategories } from 'app/entities/category/category.reducer';
import { ITag } from 'app/shared/model/tag.model';
import { getEntities as getTags } from 'app/entities/tag/tag.reducer';
import { IAuthor } from 'app/shared/model/author.model';
import { getEntities as getAuthors } from 'app/entities/author/author.reducer';
import { IAd } from 'app/shared/model/ad.model';
import { getEntity, updateEntity, createEntity, reset } from './ad.reducer';

export const AdUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const categories = useAppSelector(state => state.category.entities);
  const tags = useAppSelector(state => state.tag.entities);
  const authors = useAppSelector(state => state.author.entities);
  const adEntity = useAppSelector(state => state.ad.entity);
  const loading = useAppSelector(state => state.ad.loading);
  const updating = useAppSelector(state => state.ad.updating);
  const updateSuccess = useAppSelector(state => state.ad.updateSuccess);

  const handleClose = () => {
    navigate('/ad');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
    dispatch(getCategories({}));
    dispatch(getTags({}));
    dispatch(getAuthors({}));
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
      ...adEntity,
      ...values,
      categories: mapIdList(values.categories),
      tags: mapIdList(values.tags),
      authors: mapIdList(values.authors),
      owner: users.find(it => it.id.toString() === values.owner.toString()),
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
          ...adEntity,
          createdAt: convertDateTimeFromServer(adEntity.createdAt),
          updatedAt: convertDateTimeFromServer(adEntity.updatedAt),
          owner: adEntity?.owner?.id,
          categories: adEntity?.categories?.map(e => e.id.toString()),
          tags: adEntity?.tags?.map(e => e.id.toString()),
          authors: adEntity?.authors?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="mensagemDoDiaApp.ad.home.createOrEditLabel" data-cy="AdCreateUpdateHeading">
            <Translate contentKey="mensagemDoDiaApp.ad.home.createOrEditLabel">Create or edit a Ad</Translate>
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
                <ValidatedField name="id" required readOnly id="ad-id" label={translate('global.field.id')} validate={{ required: true }} />
              ) : null}
              <ValidatedField
                label={translate('mensagemDoDiaApp.ad.createdAt')}
                id="ad-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('mensagemDoDiaApp.ad.updatedAt')}
                id="ad-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('mensagemDoDiaApp.ad.locale')}
                id="ad-locale"
                name="locale"
                data-cy="locale"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('mensagemDoDiaApp.ad.deviceType')}
                id="ad-deviceType"
                name="deviceType"
                data-cy="deviceType"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('mensagemDoDiaApp.ad.featured')}
                id="ad-featured"
                name="featured"
                data-cy="featured"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('mensagemDoDiaApp.ad.active')}
                id="ad-active"
                name="active"
                data-cy="active"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('mensagemDoDiaApp.ad.affiliateLink')}
                id="ad-affiliateLink"
                name="affiliateLink"
                data-cy="affiliateLink"
                type="text"
              />
              <ValidatedField id="ad-owner" name="owner" data-cy="owner" label={translate('mensagemDoDiaApp.ad.owner')} type="select">
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('mensagemDoDiaApp.ad.category')}
                id="ad-category"
                data-cy="category"
                type="select"
                multiple
                name="categories"
              >
                <option value="" key="0" />
                {categories
                  ? categories.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField label={translate('mensagemDoDiaApp.ad.tag')} id="ad-tag" data-cy="tag" type="select" multiple name="tags">
                <option value="" key="0" />
                {tags
                  ? tags.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('mensagemDoDiaApp.ad.author')}
                id="ad-author"
                data-cy="author"
                type="select"
                multiple
                name="authors"
              >
                <option value="" key="0" />
                {authors
                  ? authors.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/ad" replace color="info">
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

export default AdUpdate;
