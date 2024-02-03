import category from 'app/entities/category/category.reducer';
import tag from 'app/entities/tag/tag.reducer';
import phrase from 'app/entities/phrase/phrase.reducer';
import author from 'app/entities/author/author.reducer';
import ad from 'app/entities/ad/ad.reducer';
import media from 'app/entities/media/media.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  category,
  tag,
  phrase,
  author,
  ad,
  media,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
