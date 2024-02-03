import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './media.reducer';

export const MediaDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const mediaEntity = useAppSelector(state => state.media.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="mediaDetailsHeading">
          <Translate contentKey="mensagemDoDiaApp.media.detail.title">Media</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{mediaEntity.id}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="mensagemDoDiaApp.media.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{mediaEntity.createdAt ? <TextFormat value={mediaEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="mensagemDoDiaApp.media.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>{mediaEntity.updatedAt ? <TextFormat value={mediaEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="mensagemDoDiaApp.media.type">Type</Translate>
            </span>
          </dt>
          <dd>{mediaEntity.type}</dd>
          <dt>
            <span id="url">
              <Translate contentKey="mensagemDoDiaApp.media.url">Url</Translate>
            </span>
          </dt>
          <dd>{mediaEntity.url}</dd>
          <dt>
            <span id="width">
              <Translate contentKey="mensagemDoDiaApp.media.width">Width</Translate>
            </span>
          </dt>
          <dd>{mediaEntity.width}</dd>
          <dt>
            <span id="height">
              <Translate contentKey="mensagemDoDiaApp.media.height">Height</Translate>
            </span>
          </dt>
          <dd>{mediaEntity.height}</dd>
          <dt>
            <span id="active">
              <Translate contentKey="mensagemDoDiaApp.media.active">Active</Translate>
            </span>
          </dt>
          <dd>{mediaEntity.active ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="mensagemDoDiaApp.media.owner">Owner</Translate>
          </dt>
          <dd>{mediaEntity.owner ? mediaEntity.owner.id : ''}</dd>
          <dt>
            <Translate contentKey="mensagemDoDiaApp.media.phrase">Phrase</Translate>
          </dt>
          <dd>{mediaEntity.phrase ? mediaEntity.phrase.id : ''}</dd>
          <dt>
            <Translate contentKey="mensagemDoDiaApp.media.ad">Ad</Translate>
          </dt>
          <dd>{mediaEntity.ad ? mediaEntity.ad.id : ''}</dd>
          <dt>
            <Translate contentKey="mensagemDoDiaApp.media.category">Category</Translate>
          </dt>
          <dd>{mediaEntity.category ? mediaEntity.category.id : ''}</dd>
          <dt>
            <Translate contentKey="mensagemDoDiaApp.media.tag">Tag</Translate>
          </dt>
          <dd>{mediaEntity.tag ? mediaEntity.tag.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/media" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/media/${mediaEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MediaDetail;
