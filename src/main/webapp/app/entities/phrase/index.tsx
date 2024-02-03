import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Phrase from './phrase';
import PhraseDetail from './phrase-detail';
import PhraseUpdate from './phrase-update';
import PhraseDeleteDialog from './phrase-delete-dialog';

const PhraseRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Phrase />} />
    <Route path="new" element={<PhraseUpdate />} />
    <Route path=":id">
      <Route index element={<PhraseDetail />} />
      <Route path="edit" element={<PhraseUpdate />} />
      <Route path="delete" element={<PhraseDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default PhraseRoutes;
