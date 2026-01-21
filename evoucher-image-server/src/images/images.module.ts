import { Module } from '@nestjs/common';
import { ImagesService } from './images.service';
import { ImagesController } from './images.controller';
import { HttpModule } from '@nestjs/axios';
import { UploadsService } from "../uploads.service";

@Module({
  controllers: [ImagesController],
  providers: [ImagesService, UploadsService],
  imports: [HttpModule],
})
export class ImagesModule {}
