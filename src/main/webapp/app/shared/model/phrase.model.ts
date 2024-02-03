import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IAuthor } from 'app/shared/model/author.model';
import { IMedia } from 'app/shared/model/media.model';
import { ICategory } from 'app/shared/model/category.model';
import { ITag } from 'app/shared/model/tag.model';

export interface IPhrase {
  id?: number;
  content?: string;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs;
  featured?: boolean;
  active?: boolean;
  slug?: string;
  owner?: IUser | null;
  author?: IAuthor | null;
  media?: IMedia[] | null;
  categories?: ICategory[] | null;
  tags?: ITag[] | null;
}

export const defaultValue: Readonly<IPhrase> = {
  featured: false,
  active: false,
};
