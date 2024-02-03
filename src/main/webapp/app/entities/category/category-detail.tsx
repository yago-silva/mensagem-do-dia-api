import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './category.reducer';

export const CategoryDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const categoryEntity = useAppSelector(state => state.category.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="categoryDetailsHeading">
          <Translate contentKey="mensagemDoDiaApp.category.detail.title">Category</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{categoryEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="mensagemDoDiaApp.category.name">Name</Translate>
            </span>
          </dt>
          <dd>{categoryEntity.name}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="mensagemDoDiaApp.category.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{categoryEntity.createdAt ? <TextFormat value={categoryEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="mensagemDoDiaApp.category.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>{categoryEntity.updatedAt ? <TextFormat value={categoryEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="featured">
              <Translate contentKey="mensagemDoDiaApp.category.featured">Featured</Translate>
            </span>
          </dt>
          <dd>{categoryEntity.featured ? 'true' : 'false'}</dd>
          <dt>
            <span id="active">
              <Translate contentKey="mensagemDoDiaApp.category.active">Active</Translate>
            </span>
          </dt>
          <dd>{categoryEntity.active ? 'true' : 'false'}</dd>
          <dt>
            <span id="slug">
              <Translate contentKey="mensagemDoDiaApp.category.slug">Slug</Translate>
            </span>
          </dt>
          <dd>{categoryEntity.slug}</dd>
          <dt>
            <Translate contentKey="mensagemDoDiaApp.category.owner">Owner</Translate>
          </dt>
          <dd>{categoryEntity.owner ? categoryEntity.owner.id : ''}</dd>
          <dt>
            <Translate contentKey="mensagemDoDiaApp.category.category">Category</Translate>
          </dt>
          <dd>{categoryEntity.category ? categoryEntity.category.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/category" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/category/${categoryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CategoryDetail;
