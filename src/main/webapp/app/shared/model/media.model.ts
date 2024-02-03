import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IPhrase } from 'app/shared/model/phrase.model';
import { IAd } from 'app/shared/model/ad.model';
import { ICategory } from 'app/shared/model/category.model';
import { ITag } from 'app/shared/model/tag.model';
import { MediaType } from 'app/shared/model/enumerations/media-type.model';

export interface IMedia {
  id?: number;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs;
  type?: keyof typeof MediaType;
  url?: string;
  width?: number;
  height?: number;
  active?: boolean;
  owner?: IUser | null;
  phrase?: IPhrase | null;
  ad?: IAd | null;
  category?: ICategory | null;
  tag?: ITag | null;
}

export const defaultValue: Readonly<IMedia> = {
  active: false,
};
