FROM nginx:1.27-alpine
COPY ./conf/nginx.conf /etc/nginx/nginx.conf
EXPOSE 8000
CMD ["nginx", "-g", "daemon off;"]