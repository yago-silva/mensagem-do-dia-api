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

import { getEntities } from './media.reducer';

export const Media = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const mediaList = useAppSelector(state => state.media.entities);
  const loading = useAppSelector(state => state.media.loading);

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
      <h2 id="media-heading" data-cy="MediaHeading">
        <Translate contentKey="mensagemDoDiaApp.media.home.title">Media</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="mensagemDoDiaApp.media.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/media/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="mensagemDoDiaApp.media.home.createLabel">Create new Media</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {mediaList && mediaList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="mensagemDoDiaApp.media.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('createdAt')}>
                  <Translate contentKey="mensagemDoDiaApp.media.createdAt">Created At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdAt')} />
                </th>
                <th className="hand" onClick={sort('updatedAt')}>
                  <Translate contentKey="mensagemDoDiaApp.media.updatedAt">Updated At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('updatedAt')} />
                </th>
                <th className="hand" onClick={sort('type')}>
                  <Translate contentKey="mensagemDoDiaApp.media.type">Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('type')} />
                </th>
                <th className="hand" onClick={sort('url')}>
                  <Translate contentKey="mensagemDoDiaApp.media.url">Url</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('url')} />
                </th>
                <th className="hand" onClick={sort('width')}>
                  <Translate contentKey="mensagemDoDiaApp.media.width">Width</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('width')} />
                </th>
                <th className="hand" onClick={sort('height')}>
                  <Translate contentKey="mensagemDoDiaApp.media.height">Height</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('height')} />
                </th>
                <th className="hand" onClick={sort('active')}>
                  <Translate contentKey="mensagemDoDiaApp.media.active">Active</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('active')} />
                </th>
                <th>
                  <Translate contentKey="mensagemDoDiaApp.media.owner">Owner</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="mensagemDoDiaApp.media.phrase">Phrase</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="mensagemDoDiaApp.media.ad">Ad</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="mensagemDoDiaApp.media.category">Category</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="mensagemDoDiaApp.media.tag">Tag</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {mediaList.map((media, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/media/${media.id}`} color="link" size="sm">
                      {media.id}
                    </Button>
                  </td>
                  <td>{media.createdAt ? <TextFormat type="date" value={media.createdAt} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{media.updatedAt ? <TextFormat type="date" value={media.updatedAt} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>
                    <Translate contentKey={`mensagemDoDiaApp.MediaType.${media.type}`} />
                  </td>
                  <td>{media.url}</td>
                  <td>{media.width}</td>
                  <td>{media.height}</td>
                  <td>{media.active ? 'true' : 'false'}</td>
                  <td>{media.owner ? media.owner.id : ''}</td>
                  <td>{media.phrase ? <Link to={`/phrase/${media.phrase.id}`}>{media.phrase.id}</Link> : ''}</td>
                  <td>{media.ad ? <Link to={`/ad/${media.ad.id}`}>{media.ad.id}</Link> : ''}</td>
                  <td>{media.category ? <Link to={`/category/${media.category.id}`}>{media.category.id}</Link> : ''}</td>
                  <td>{media.tag ? <Link to={`/tag/${media.tag.id}`}>{media.tag.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/media/${media.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/media/${media.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/media/${media.id}/delete`)}
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
              <Translate contentKey="mensagemDoDiaApp.media.home.notFound">No Media found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Media;
