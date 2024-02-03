import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Ad from './ad';
import AdDetail from './ad-detail';
import AdUpdate from './ad-update';
import AdDeleteDialog from './ad-delete-dialog';

const AdRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Ad />} />
    <Route path="new" element={<AdUpdate />} />
    <Route path=":id">
      <Route index element={<AdDetail />} />
      <Route path="edit" element={<AdUpdate />} />
      <Route path="delete" element={<AdDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AdRoutes;
