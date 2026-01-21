import { Settings } from '@/types';
import settings from '@/data_temp/settings.json';

export const settingsClient = {
  getData({ language }: { language: string }) {
    const settingsData: Settings = settings as Settings;
    return Promise.resolve(settingsData);
  },
};
