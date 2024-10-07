1. Handling Image Upload
   When a user uploads an image from the frontend (Angular), the image is sent to the backend (Spring Boot) using a multipart/form-data request. The backend processes the image, saves it in the file system or cloud storage, and stores metadata such as the file name and URL in the database.

Frontend: The Angular application allows users to select an image and send it to the backend via a service using HttpClient.
Backend: The Spring Boot application receives the image through a REST endpoint. The image is stored on the server, and the image URL is saved in the database. If needed, validation checks (e.g., file size, format) are performed.
2. Batch Image Upload
   Batch upload allows multiple images to be uploaded at once. The frontend sends multiple files in a single request, which are then processed by the backend. Each image is handled individually, and all metadata for the uploaded images is stored in the database.

Frontend: Users can select multiple images, and the application sends them in a single request to the backend.
Backend: The backend processes each file in the batch, saves them, and returns the corresponding URLs or metadata to the frontend.
3. Image Compression
   To optimize performance and save storage space, image compression is implemented in the backend. When an image is uploaded, it is compressed to reduce its file size before being saved. This reduces bandwidth usage when serving images to clients.

Compression: Images are resized or compressed to a lower quality to balance file size and visual quality. The compression can be customized to adjust the level of quality or dimensions of the image based on the project needs.
4. Saving Images
   The images are saved on the server or a cloud storage platform (like AWS S3 or Google Cloud Storage). Only the file path or URL is stored in the database for easy retrieval. The saved images are typically organized by folders or date to keep the file system structured.

File System: The image files are stored locally on the server under specific directories.
Cloud Storage: If using cloud storage, the images are uploaded directly to the cloud, and the public URL is saved in the database for access.
5. Updating Database Records
   Each time an image is uploaded, metadata such as the file name, file path (or URL), and related information (e.g., upload time) is stored in the database. When images are updated (re-uploaded or replaced), the database records are updated with the new file information.

Metadata Storage: The database stores metadata like the image URL, file name, and any associated information (e.g., user or item ID).
Update Records: If an image needs to be replaced, the old image can be deleted, and the new one is saved in its place, with the database record updated accordingly.
6. Loading Images on the UI
   Once images are uploaded, they can be displayed on the UI. The Angular frontend retrieves image URLs from the backend and renders them in the application using <img> tags or other appropriate components.

Retrieving Images: The frontend fetches the stored image URLs from the backend via an API call.
Displaying Images: The URLs are used in HTML to display the images. This is typically done by binding the image URL to an <img> tag for each image.
Summary of Flow
Image Upload: Angular sends the image(s) via HTTP requests to the Spring Boot backend.
Processing: Spring Boot compresses the images, saves them to the server or cloud storage, and updates the database with metadata.
Batch Upload: For multiple files, the process is repeated for each image in the batch.
Image Display: The Angular frontend retrieves and displays the images by accessing their URLs from the backend.
