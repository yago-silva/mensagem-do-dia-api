import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './author.reducer';

export const AuthorDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const authorEntity = useAppSelector(state => state.author.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="authorDetailsHeading">
          <Translate contentKey="mensagemDoDiaApp.author.detail.title">Author</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{authorEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="mensagemDoDiaApp.author.name">Name</Translate>
            </span>
          </dt>
          <dd>{authorEntity.name}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="mensagemDoDiaApp.author.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{authorEntity.createdAt ? <TextFormat value={authorEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="mensagemDoDiaApp.author.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>{authorEntity.updatedAt ? <TextFormat value={authorEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="featured">
              <Translate contentKey="mensagemDoDiaApp.author.featured">Featured</Translate>
            </span>
          </dt>
          <dd>{authorEntity.featured ? 'true' : 'false'}</dd>
          <dt>
            <span id="active">
              <Translate contentKey="mensagemDoDiaApp.author.active">Active</Translate>
            </span>
          </dt>
          <dd>{authorEntity.active ? 'true' : 'false'}</dd>
          <dt>
            <span id="slug">
              <Translate contentKey="mensagemDoDiaApp.author.slug">Slug</Translate>
            </span>
          </dt>
          <dd>{authorEntity.slug}</dd>
          <dt>
            <Translate contentKey="mensagemDoDiaApp.author.owner">Owner</Translate>
          </dt>
          <dd>{authorEntity.owner ? authorEntity.owner.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/author" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/author/${authorEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AuthorDetail;
