import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IMedia } from 'app/shared/model/media.model';
import { ICategory } from 'app/shared/model/category.model';
import { IPhrase } from 'app/shared/model/phrase.model';
import { IAd } from 'app/shared/model/ad.model';

export interface ITag {
  id?: number;
  name?: string;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs;
  featured?: boolean;
  active?: boolean;
  slug?: string;
  owner?: IUser | null;
  media?: IMedia[] | null;
  categories?: ICategory[] | null;
  phrases?: IPhrase[] | null;
  ads?: IAd[] | null;
}

export const defaultValue: Readonly<ITag> = {
  featured: false,
  active: false,
};
