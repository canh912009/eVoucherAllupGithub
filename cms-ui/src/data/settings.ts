import { useQuery } from 'react-query';
import { settingsClient } from './client/settings';
import { Settings } from '@/types';

export const useSettingsQuery = ({ language }: { language: string }) => {
  const { data, error, isLoading } = useQuery<Settings, Error>(
    ['settings', { language }],
    () => settingsClient.getData({ language })
  );

  return {
    settings: data ?? {},
    error,
    loading: isLoading,
  };
};
