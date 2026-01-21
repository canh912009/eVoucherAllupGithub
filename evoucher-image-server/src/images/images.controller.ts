import {
  Controller,
  Post,
  UseInterceptors,
  UploadedFile,
  UploadedFiles,
  Logger,
  createParamDecorator,
  ExecutionContext,
} from '@nestjs/common';
import { FileInterceptor, FilesInterceptor } from '@nestjs/platform-express';
import { storageImages, validateImageFile } from '../storage.config';
import { FILE_UPLOADS_MAX_NUMBER } from '../config/config';
import { ApiTags } from '@nestjs/swagger';
import { UploadsService } from '../uploads.service';

const AuthToken = createParamDecorator(
  (data: unknown, ctx: ExecutionContext) => {
    const request = ctx.switchToHttp().getRequest();
    return request.headers.authorization;
  },
);

@Controller('images')
@ApiTags('images')
export class ImagesController {
  private readonly logger = new Logger(ImagesController.name);
  constructor(private readonly uploadsService: UploadsService) {}

  @Post('upload')
  @UseInterceptors(
    FileInterceptor('image', {
      storage: storageImages,
      fileFilter: validateImageFile, // Add file filter
      limits: { fileSize: 10 * 1024 * 1024 }, // 10MB limit
    }),
  )
  async uploadSingle(@UploadedFile() file) {
    await this.uploadsService.checkFile(file);

    // console.log(file);
    this.logger.log(file);

    const res = { path: '/static/images/' + file?.filename };
    return res;
  }

  @Post('uploads')
  @UseInterceptors(
    FilesInterceptor('images', FILE_UPLOADS_MAX_NUMBER, {
      storage: storageImages,
      fileFilter: validateImageFile, // Add file filter
      limits: { fileSize: 10 * 1024 * 1024 }, // 10MB limit
    }),
  )
  async uploadMultiple(@UploadedFiles() files) {
    await this.uploadsService.checkFile(files);

    // console.log(files);
    this.logger.log('files', files);

    const res = [];
    files.forEach((file) => {
      res.push({ path: '/static/images/' + file?.filename });
    });
    return res;
  }
}
