import {
  BadRequestException,
  ForbiddenException,
  Injectable,
  UnauthorizedException,
} from '@nestjs/common';
import { HttpService } from '@nestjs/axios';
import { map, catchError, lastValueFrom } from 'rxjs';
import { API_ME } from './config/config';

@Injectable()
export class UploadsService {
  constructor(private http: HttpService) {}

  async getMeApiMessage(token: string) {
    const headers = {
      Authorization: `Bearer ${token}`,
    };
    const request = this.http
      .get(API_ME, { headers })
      .pipe(map((res) => res.data?.message))
      .pipe(
        catchError(() => {
          throw new ForbiddenException('API not available');
        }),
      );

    return await lastValueFrom(request);
  }

  public async checkFileAndToken(file: Express.Multer.File, token: string) {
    if (!file) {
      throw new BadRequestException('No files uploaded.');
    }
    if (!token) {
      throw new BadRequestException('Bearer token is missing.');
    }

    const result = await this.getMeApiMessage(token);
    console.log('result', result);
    if (result !== 'OK') {
      throw new UnauthorizedException(
        'upload : Not authorized with the provided token.',
      );
    }
  }

  public async checkFile(file: Express.Multer.File) {
    if (!file) {
      throw new BadRequestException('File is required');
    }
  }
}
