import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IMedia } from 'app/shared/model/media.model';
import { ICategory } from 'app/shared/model/category.model';
import { ITag } from 'app/shared/model/tag.model';
import { IAuthor } from 'app/shared/model/author.model';

export interface IAd {
  id?: number;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs;
  locale?: string;
  deviceType?: string;
  featured?: boolean;
  active?: boolean;
  affiliateLink?: string | null;
  owner?: IUser | null;
  media?: IMedia[] | null;
  categories?: ICategory[] | null;
  tags?: ITag[] | null;
  authors?: IAuthor[] | null;
}

export const defaultValue: Readonly<IAd> = {
  featured: false,
  active: false,
};
