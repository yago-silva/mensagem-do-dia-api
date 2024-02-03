import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IMedia } from 'app/shared/model/media.model';
import { IPhrase } from 'app/shared/model/phrase.model';
import { IAd } from 'app/shared/model/ad.model';
import { ITag } from 'app/shared/model/tag.model';

export interface ICategory {
  id?: number;
  name?: string;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs;
  featured?: boolean;
  active?: boolean;
  slug?: string;
  owner?: IUser | null;
  parents?: ICategory[] | null;
  media?: IMedia[] | null;
  phrases?: IPhrase[] | null;
  ads?: IAd[] | null;
  tags?: ITag[] | null;
  category?: ICategory | null;
}

export const defaultValue: Readonly<ICategory> = {
  featured: false,
  active: false,
};
