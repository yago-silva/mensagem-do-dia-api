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

import { getEntities } from './phrase.reducer';

export const Phrase = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const phraseList = useAppSelector(state => state.phrase.entities);
  const loading = useAppSelector(state => state.phrase.loading);

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
      <h2 id="phrase-heading" data-cy="PhraseHeading">
        <Translate contentKey="mensagemDoDiaApp.phrase.home.title">Phrases</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="mensagemDoDiaApp.phrase.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/phrase/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="mensagemDoDiaApp.phrase.home.createLabel">Create new Phrase</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {phraseList && phraseList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="mensagemDoDiaApp.phrase.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('content')}>
                  <Translate contentKey="mensagemDoDiaApp.phrase.content">Content</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('content')} />
                </th>
                <th className="hand" onClick={sort('createdAt')}>
                  <Translate contentKey="mensagemDoDiaApp.phrase.createdAt">Created At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdAt')} />
                </th>
                <th className="hand" onClick={sort('updatedAt')}>
                  <Translate contentKey="mensagemDoDiaApp.phrase.updatedAt">Updated At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('updatedAt')} />
                </th>
                <th className="hand" onClick={sort('featured')}>
                  <Translate contentKey="mensagemDoDiaApp.phrase.featured">Featured</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('featured')} />
                </th>
                <th className="hand" onClick={sort('active')}>
                  <Translate contentKey="mensagemDoDiaApp.phrase.active">Active</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('active')} />
                </th>
                <th className="hand" onClick={sort('slug')}>
                  <Translate contentKey="mensagemDoDiaApp.phrase.slug">Slug</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('slug')} />
                </th>
                <th>
                  <Translate contentKey="mensagemDoDiaApp.phrase.owner">Owner</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="mensagemDoDiaApp.phrase.author">Author</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="mensagemDoDiaApp.phrase.category">Category</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="mensagemDoDiaApp.phrase.tag">Tag</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {phraseList.map((phrase, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/phrase/${phrase.id}`} color="link" size="sm">
                      {phrase.id}
                    </Button>
                  </td>
                  <td>{phrase.content}</td>
                  <td>{phrase.createdAt ? <TextFormat type="date" value={phrase.createdAt} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{phrase.updatedAt ? <TextFormat type="date" value={phrase.updatedAt} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{phrase.featured ? 'true' : 'false'}</td>
                  <td>{phrase.active ? 'true' : 'false'}</td>
                  <td>{phrase.slug}</td>
                  <td>{phrase.owner ? phrase.owner.id : ''}</td>
                  <td>{phrase.author ? <Link to={`/author/${phrase.author.id}`}>{phrase.author.id}</Link> : ''}</td>
                  <td>
                    {phrase.categories
                      ? phrase.categories.map((val, j) => (
                          <span key={j}>
                            <Link to={`/category/${val.id}`}>{val.id}</Link>
                            {j === phrase.categories.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td>
                    {phrase.tags
                      ? phrase.tags.map((val, j) => (
                          <span key={j}>
                            <Link to={`/tag/${val.id}`}>{val.id}</Link>
                            {j === phrase.tags.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/phrase/${phrase.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/phrase/${phrase.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/phrase/${phrase.id}/delete`)}
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
              <Translate contentKey="mensagemDoDiaApp.phrase.home.notFound">No Phrases found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Phrase;
