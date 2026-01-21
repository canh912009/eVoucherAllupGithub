import { diskStorage } from 'multer';
import { PUBLIC_FOLDER_UPLOAD, PRIVATE_FOLDER_UPLOAD } from './config/config';
import { HttpException, HttpStatus } from '@nestjs/common';
import { extname } from 'path';

export const storageImages = diskStorage({
  destination: PUBLIC_FOLDER_UPLOAD + '/images',
  filename: (req, file, callback) => {
    callback(null, generateFilename(file));
  },
});

// Validate file type
export const validateImageFile = (req, file, callback) => {
  const allowedTypes = ['.jpg', '.jpeg', '.png', '.gif', '.svg'];
  const fileExt = extname(file.originalname).toLowerCase();

  if (allowedTypes.includes(fileExt)) {
    callback(null, true);
  } else {
    callback(
      new HttpException(
        'Only images (jpg, jpeg, png, gif, svg) are allowed!',
        HttpStatus.BAD_REQUEST,
      ),
      false,
    );
  }
};

export const storageDocuments = diskStorage({
  destination: PRIVATE_FOLDER_UPLOAD + '/documents',
  filename: (req, file, callback) => {
    callback(null, generateFilename(file));
  },
});

function generateFilename(file) {
  // return `${Date.now()}.${file.originalname}`;

  const encodedName = Buffer.from(file.originalname).toString('base64');
  const fileExt = extname(file.originalname);
  return `${Date.now()}.${encodedName.substring(0, 10)}${fileExt}`;
}
