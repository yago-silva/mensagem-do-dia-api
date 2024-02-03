import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Category from './category';
import Tag from './tag';
import Phrase from './phrase';
import Author from './author';
import Ad from './ad';
import Media from './media';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="category/*" element={<Category />} />
        <Route path="tag/*" element={<Tag />} />
        <Route path="phrase/*" element={<Phrase />} />
        <Route path="author/*" element={<Author />} />
        <Route path="ad/*" element={<Ad />} />
        <Route path="media/*" element={<Media />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
