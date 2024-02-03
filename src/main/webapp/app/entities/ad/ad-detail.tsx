import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './ad.reducer';

export const AdDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const adEntity = useAppSelector(state => state.ad.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="adDetailsHeading">
          <Translate contentKey="mensagemDoDiaApp.ad.detail.title">Ad</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{adEntity.id}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="mensagemDoDiaApp.ad.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{adEntity.createdAt ? <TextFormat value={adEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="mensagemDoDiaApp.ad.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>{adEntity.updatedAt ? <TextFormat value={adEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="locale">
              <Translate contentKey="mensagemDoDiaApp.ad.locale">Locale</Translate>
            </span>
          </dt>
          <dd>{adEntity.locale}</dd>
          <dt>
            <span id="deviceType">
              <Translate contentKey="mensagemDoDiaApp.ad.deviceType">Device Type</Translate>
            </span>
          </dt>
          <dd>{adEntity.deviceType}</dd>
          <dt>
            <span id="featured">
              <Translate contentKey="mensagemDoDiaApp.ad.featured">Featured</Translate>
            </span>
          </dt>
          <dd>{adEntity.featured ? 'true' : 'false'}</dd>
          <dt>
            <span id="active">
              <Translate contentKey="mensagemDoDiaApp.ad.active">Active</Translate>
            </span>
          </dt>
          <dd>{adEntity.active ? 'true' : 'false'}</dd>
          <dt>
            <span id="affiliateLink">
              <Translate contentKey="mensagemDoDiaApp.ad.affiliateLink">Affiliate Link</Translate>
            </span>
          </dt>
          <dd>{adEntity.affiliateLink}</dd>
          <dt>
            <Translate contentKey="mensagemDoDiaApp.ad.owner">Owner</Translate>
          </dt>
          <dd>{adEntity.owner ? adEntity.owner.id : ''}</dd>
          <dt>
            <Translate contentKey="mensagemDoDiaApp.ad.category">Category</Translate>
          </dt>
          <dd>
            {adEntity.categories
              ? adEntity.categories.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {adEntity.categories && i === adEntity.categories.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="mensagemDoDiaApp.ad.tag">Tag</Translate>
          </dt>
          <dd>
            {adEntity.tags
              ? adEntity.tags.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {adEntity.tags && i === adEntity.tags.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="mensagemDoDiaApp.ad.author">Author</Translate>
          </dt>
          <dd>
            {adEntity.authors
              ? adEntity.authors.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {adEntity.authors && i === adEntity.authors.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/ad" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/ad/${adEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AdDetail;
