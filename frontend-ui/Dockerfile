# pull the base image
FROM node:16-alpine as builder
# set the working direction
WORKDIR /app
#install app dependencies
COPY package.json package-lock.json
COPY . .
RUN npm install --legacy-peer-deps
RUN npm run build

FROM nginx:1.21.0-alpine as runner
COPY default.conf /etc/nginx/conf.d/default.conf 
COPY --from=builder /app/build /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
