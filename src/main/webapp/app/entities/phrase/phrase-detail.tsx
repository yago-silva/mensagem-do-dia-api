import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './phrase.reducer';

export const PhraseDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const phraseEntity = useAppSelector(state => state.phrase.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="phraseDetailsHeading">
          <Translate contentKey="mensagemDoDiaApp.phrase.detail.title">Phrase</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{phraseEntity.id}</dd>
          <dt>
            <span id="content">
              <Translate contentKey="mensagemDoDiaApp.phrase.content">Content</Translate>
            </span>
          </dt>
          <dd>{phraseEntity.content}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="mensagemDoDiaApp.phrase.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{phraseEntity.createdAt ? <TextFormat value={phraseEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="mensagemDoDiaApp.phrase.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>{phraseEntity.updatedAt ? <TextFormat value={phraseEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="featured">
              <Translate contentKey="mensagemDoDiaApp.phrase.featured">Featured</Translate>
            </span>
          </dt>
          <dd>{phraseEntity.featured ? 'true' : 'false'}</dd>
          <dt>
            <span id="active">
              <Translate contentKey="mensagemDoDiaApp.phrase.active">Active</Translate>
            </span>
          </dt>
          <dd>{phraseEntity.active ? 'true' : 'false'}</dd>
          <dt>
            <span id="slug">
              <Translate contentKey="mensagemDoDiaApp.phrase.slug">Slug</Translate>
            </span>
          </dt>
          <dd>{phraseEntity.slug}</dd>
          <dt>
            <Translate contentKey="mensagemDoDiaApp.phrase.owner">Owner</Translate>
          </dt>
          <dd>{phraseEntity.owner ? phraseEntity.owner.id : ''}</dd>
          <dt>
            <Translate contentKey="mensagemDoDiaApp.phrase.author">Author</Translate>
          </dt>
          <dd>{phraseEntity.author ? phraseEntity.author.id : ''}</dd>
          <dt>
            <Translate contentKey="mensagemDoDiaApp.phrase.category">Category</Translate>
          </dt>
          <dd>
            {phraseEntity.categories
              ? phraseEntity.categories.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {phraseEntity.categories && i === phraseEntity.categories.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="mensagemDoDiaApp.phrase.tag">Tag</Translate>
          </dt>
          <dd>
            {phraseEntity.tags
              ? phraseEntity.tags.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {phraseEntity.tags && i === phraseEntity.tags.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/phrase" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/phrase/${phraseEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PhraseDetail;
