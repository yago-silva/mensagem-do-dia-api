import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IPhrase } from 'app/shared/model/phrase.model';
import { IAd } from 'app/shared/model/ad.model';

export interface IAuthor {
  id?: number;
  name?: string;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs;
  featured?: boolean;
  active?: boolean;
  slug?: string;
  owner?: IUser | null;
  phrase?: IPhrase | null;
  ads?: IAd[] | null;
}

export const defaultValue: Readonly<IAuthor> = {
  featured: false,
  active: false,
};
