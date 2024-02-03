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
import { IPhrase } from 'app/shared/model/phrase.model';
import { getEntities as getPhrases } from 'app/entities/phrase/phrase.reducer';
import { IAd } from 'app/shared/model/ad.model';
import { getEntities as getAds } from 'app/entities/ad/ad.reducer';
import { ICategory } from 'app/shared/model/category.model';
import { getEntities as getCategories } from 'app/entities/category/category.reducer';
import { ITag } from 'app/shared/model/tag.model';
import { getEntities as getTags } from 'app/entities/tag/tag.reducer';
import { IMedia } from 'app/shared/model/media.model';
import { MediaType } from 'app/shared/model/enumerations/media-type.model';
import { getEntity, updateEntity, createEntity, reset } from './media.reducer';

export const MediaUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const phrases = useAppSelector(state => state.phrase.entities);
  const ads = useAppSelector(state => state.ad.entities);
  const categories = useAppSelector(state => state.category.entities);
  const tags = useAppSelector(state => state.tag.entities);
  const mediaEntity = useAppSelector(state => state.media.entity);
  const loading = useAppSelector(state => state.media.loading);
  const updating = useAppSelector(state => state.media.updating);
  const updateSuccess = useAppSelector(state => state.media.updateSuccess);
  const mediaTypeValues = Object.keys(MediaType);

  const handleClose = () => {
    navigate('/media');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
    dispatch(getPhrases({}));
    dispatch(getAds({}));
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
    if (values.width !== undefined && typeof values.width !== 'number') {
      values.width = Number(values.width);
    }
    if (values.height !== undefined && typeof values.height !== 'number') {
      values.height = Number(values.height);
    }

    const entity = {
      ...mediaEntity,
      ...values,
      owner: users.find(it => it.id.toString() === values.owner.toString()),
      phrase: phrases.find(it => it.id.toString() === values.phrase.toString()),
      ad: ads.find(it => it.id.toString() === values.ad.toString()),
      category: categories.find(it => it.id.toString() === values.category.toString()),
      tag: tags.find(it => it.id.toString() === values.tag.toString()),
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
          type: 'IMAGE',
          ...mediaEntity,
          createdAt: convertDateTimeFromServer(mediaEntity.createdAt),
          updatedAt: convertDateTimeFromServer(mediaEntity.updatedAt),
          owner: mediaEntity?.owner?.id,
          phrase: mediaEntity?.phrase?.id,
          ad: mediaEntity?.ad?.id,
          category: mediaEntity?.category?.id,
          tag: mediaEntity?.tag?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="mensagemDoDiaApp.media.home.createOrEditLabel" data-cy="MediaCreateUpdateHeading">
            <Translate contentKey="mensagemDoDiaApp.media.home.createOrEditLabel">Create or edit a Media</Translate>
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
                  id="media-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('mensagemDoDiaApp.media.createdAt')}
                id="media-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('mensagemDoDiaApp.media.updatedAt')}
                id="media-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField label={translate('mensagemDoDiaApp.media.type')} id="media-type" name="type" data-cy="type" type="select">
                {mediaTypeValues.map(mediaType => (
                  <option value={mediaType} key={mediaType}>
                    {translate('mensagemDoDiaApp.MediaType.' + mediaType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('mensagemDoDiaApp.media.url')}
                id="media-url"
                name="url"
                data-cy="url"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('mensagemDoDiaApp.media.width')}
                id="media-width"
                name="width"
                data-cy="width"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('mensagemDoDiaApp.media.height')}
                id="media-height"
                name="height"
                data-cy="height"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('mensagemDoDiaApp.media.active')}
                id="media-active"
                name="active"
                data-cy="active"
                check
                type="checkbox"
              />
              <ValidatedField id="media-owner" name="owner" data-cy="owner" label={translate('mensagemDoDiaApp.media.owner')} type="select">
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
                id="media-phrase"
                name="phrase"
                data-cy="phrase"
                label={translate('mensagemDoDiaApp.media.phrase')}
                type="select"
              >
                <option value="" key="0" />
                {phrases
                  ? phrases.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField id="media-ad" name="ad" data-cy="ad" label={translate('mensagemDoDiaApp.media.ad')} type="select">
                <option value="" key="0" />
                {ads
                  ? ads.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="media-category"
                name="category"
                data-cy="category"
                label={translate('mensagemDoDiaApp.media.category')}
                type="select"
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
              <ValidatedField id="media-tag" name="tag" data-cy="tag" label={translate('mensagemDoDiaApp.media.tag')} type="select">
                <option value="" key="0" />
                {tags
                  ? tags.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/media" replace color="info">
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

export default MediaUpdate;
