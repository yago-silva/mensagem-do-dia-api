import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './tag.reducer';

export const TagDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const tagEntity = useAppSelector(state => state.tag.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="tagDetailsHeading">
          <Translate contentKey="mensagemDoDiaApp.tag.detail.title">Tag</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{tagEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="mensagemDoDiaApp.tag.name">Name</Translate>
            </span>
          </dt>
          <dd>{tagEntity.name}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="mensagemDoDiaApp.tag.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{tagEntity.createdAt ? <TextFormat value={tagEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="mensagemDoDiaApp.tag.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>{tagEntity.updatedAt ? <TextFormat value={tagEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="featured">
              <Translate contentKey="mensagemDoDiaApp.tag.featured">Featured</Translate>
            </span>
          </dt>
          <dd>{tagEntity.featured ? 'true' : 'false'}</dd>
          <dt>
            <span id="active">
              <Translate contentKey="mensagemDoDiaApp.tag.active">Active</Translate>
            </span>
          </dt>
          <dd>{tagEntity.active ? 'true' : 'false'}</dd>
          <dt>
            <span id="slug">
              <Translate contentKey="mensagemDoDiaApp.tag.slug">Slug</Translate>
            </span>
          </dt>
          <dd>{tagEntity.slug}</dd>
          <dt>
            <Translate contentKey="mensagemDoDiaApp.tag.owner">Owner</Translate>
          </dt>
          <dd>{tagEntity.owner ? tagEntity.owner.id : ''}</dd>
          <dt>
            <Translate contentKey="mensagemDoDiaApp.tag.category">Category</Translate>
          </dt>
          <dd>
            {tagEntity.categories
              ? tagEntity.categories.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {tagEntity.categories && i === tagEntity.categories.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/tag" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/tag/${tagEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default TagDetail;
