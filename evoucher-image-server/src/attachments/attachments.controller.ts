import {
  Controller,
  Post,
  UseInterceptors,
  UploadedFiles,
  UploadedFile,
  Res,
  Logger,
  Body,
  createParamDecorator,
  ExecutionContext,
} from '@nestjs/common';
import { Response } from 'express';
import * as path from 'path';
import { FileInterceptor, FilesInterceptor } from '@nestjs/platform-express';
import { storageDocuments } from './../storage.config';
import {
  PRIVATE_FOLDER_UPLOAD,
  FILE_UPLOADS_MAX_NUMBER,
} from '../config/config';
import * as fs from 'fs';
import { ApiTags } from '@nestjs/swagger';
import { UploadsService } from '../uploads.service';

const AuthToken = createParamDecorator(
  (data: unknown, ctx: ExecutionContext) => {
    const request = ctx.switchToHttp().getRequest();
    return request.headers.authorization;
  },
);

@Controller('attachments')
@ApiTags('attachments')
export class AttachmentsController {
  private readonly logger = new Logger(AttachmentsController.name);
  constructor(private readonly uploadsService: UploadsService) {}

  extractFilenameFromPath(input: string): string {
    const filename = path.basename(input);
    return filename;
  }

  @Post('upload')
  @UseInterceptors(FileInterceptor('file', { storage: storageDocuments }))
  async uploadFile(@UploadedFile() file: Express.Multer.File) {
    await this.uploadsService.checkFile(file);

    this.logger.log('file', file);

    const res = { path: '/documents/' + file?.filename };
    return res;
  }

  @Post('uploads')
  @UseInterceptors(
    FilesInterceptor('files', FILE_UPLOADS_MAX_NUMBER, {
      storage: storageDocuments,
    }),
  )
  async uploadMultiple(@UploadedFiles() files) {
    await this.uploadsService.checkFile(files);

    this.logger.log('files', files);

    const res = [];
    files.forEach((file) => {
      res.push({ path: '/documents/' + file?.filename });
    });
    return res;
  }

  @Post('download')
  async downloadFile(@Body() path, @Res() res: Response) {
    this.logger.log('path', path);
    const file = path.path;
    if (!file) {
      res.status(400).json({ message: 'What file?', errorCode: 'What file?' });
      return;
    }

    const filePath = PRIVATE_FOLDER_UPLOAD + file;
    const filename = this.extractFilenameFromPath(file);

    // console.log('filePath', filePath, 'filename', filename);
    this.logger.log('filePath', filePath, 'filename', filename);

    // res.setHeader('Content-Disposition', `attachment; filename="${file}"`);
    // res.sendFile(filePath);

    // Check if the file exists
    if (fs.existsSync(filePath)) {
      // res.download(filePath, filename);
      const fileContent = fs.readFileSync(filePath, { encoding: 'base64' });
      res.setHeader('Content-Disposition', `attachment; filename=${filename}`);
      res.send(fileContent);
    } else {
      res.status(404).json({ message: 'File not found' });
    }
  }
}
