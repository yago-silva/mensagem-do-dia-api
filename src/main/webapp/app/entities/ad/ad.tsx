import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './ad.reducer';

export const Ad = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const adList = useAppSelector(state => state.ad.entities);
  const loading = useAppSelector(state => state.ad.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="ad-heading" data-cy="AdHeading">
        <Translate contentKey="mensagemDoDiaApp.ad.home.title">Ads</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="mensagemDoDiaApp.ad.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/ad/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="mensagemDoDiaApp.ad.home.createLabel">Create new Ad</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {adList && adList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="mensagemDoDiaApp.ad.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('createdAt')}>
                  <Translate contentKey="mensagemDoDiaApp.ad.createdAt">Created At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdAt')} />
                </th>
                <th className="hand" onClick={sort('updatedAt')}>
                  <Translate contentKey="mensagemDoDiaApp.ad.updatedAt">Updated At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('updatedAt')} />
                </th>
                <th className="hand" onClick={sort('locale')}>
                  <Translate contentKey="mensagemDoDiaApp.ad.locale">Locale</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('locale')} />
                </th>
                <th className="hand" onClick={sort('deviceType')}>
                  <Translate contentKey="mensagemDoDiaApp.ad.deviceType">Device Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('deviceType')} />
                </th>
                <th className="hand" onClick={sort('featured')}>
                  <Translate contentKey="mensagemDoDiaApp.ad.featured">Featured</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('featured')} />
                </th>
                <th className="hand" onClick={sort('active')}>
                  <Translate contentKey="mensagemDoDiaApp.ad.active">Active</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('active')} />
                </th>
                <th className="hand" onClick={sort('affiliateLink')}>
                  <Translate contentKey="mensagemDoDiaApp.ad.affiliateLink">Affiliate Link</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('affiliateLink')} />
                </th>
                <th>
                  <Translate contentKey="mensagemDoDiaApp.ad.owner">Owner</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="mensagemDoDiaApp.ad.category">Category</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="mensagemDoDiaApp.ad.tag">Tag</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="mensagemDoDiaApp.ad.author">Author</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {adList.map((ad, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/ad/${ad.id}`} color="link" size="sm">
                      {ad.id}
                    </Button>
                  </td>
                  <td>{ad.createdAt ? <TextFormat type="date" value={ad.createdAt} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{ad.updatedAt ? <TextFormat type="date" value={ad.updatedAt} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{ad.locale}</td>
                  <td>{ad.deviceType}</td>
                  <td>{ad.featured ? 'true' : 'false'}</td>
                  <td>{ad.active ? 'true' : 'false'}</td>
                  <td>{ad.affiliateLink}</td>
                  <td>{ad.owner ? ad.owner.id : ''}</td>
                  <td>
                    {ad.categories
                      ? ad.categories.map((val, j) => (
                          <span key={j}>
                            <Link to={`/category/${val.id}`}>{val.id}</Link>
                            {j === ad.categories.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td>
                    {ad.tags
                      ? ad.tags.map((val, j) => (
                          <span key={j}>
                            <Link to={`/tag/${val.id}`}>{val.id}</Link>
                            {j === ad.tags.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td>
                    {ad.authors
                      ? ad.authors.map((val, j) => (
                          <span key={j}>
                            <Link to={`/author/${val.id}`}>{val.id}</Link>
                            {j === ad.authors.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/ad/${ad.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/ad/${ad.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/ad/${ad.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="mensagemDoDiaApp.ad.home.notFound">No Ads found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Ad;
