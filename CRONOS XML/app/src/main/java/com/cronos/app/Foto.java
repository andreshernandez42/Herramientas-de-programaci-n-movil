package com.cronos.app;

// datos de cada foto
public class Foto {
    private final int imagenRes;
    private final String rutaArchivo;
    private final String titulo;
    private final String descripcion;

    // fotos que vienen con la app
    public Foto(int imagenRes, String titulo, String descripcion) {
        this(imagenRes, null, titulo, descripcion);
    }

    // fotos que agrega el usuario
    public Foto(String rutaArchivo, String titulo, String descripcion) {
        this(0, rutaArchivo, titulo, descripcion);
    }

    private Foto(int imagenRes, String rutaArchivo, String titulo, String descripcion) {
        this.imagenRes = imagenRes;
        this.rutaArchivo = rutaArchivo;
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public int getImagenRes() { return imagenRes; }
    public String getRutaArchivo() { return rutaArchivo; }
    public boolean esDelUsuario() { return rutaArchivo != null; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }

    // pone la foto en la imagen
    public void cargarEn(android.widget.ImageView imageView) {
        cargarEn(imageView, 1080);
    }

    public void cargarEn(android.widget.ImageView imageView, int tamanoMaximo) {
        if (esDelUsuario()) {
            // la achica para que no se trabe
            android.graphics.BitmapFactory.Options opciones = new android.graphics.BitmapFactory.Options();
            opciones.inJustDecodeBounds = true;
            android.graphics.BitmapFactory.decodeFile(rutaArchivo, opciones);
            int escala = 1;
            while (opciones.outWidth / escala > tamanoMaximo || opciones.outHeight / escala > tamanoMaximo) escala *= 2;
            opciones.inJustDecodeBounds = false;
            opciones.inSampleSize = escala;
            android.graphics.Bitmap bitmap = android.graphics.BitmapFactory.decodeFile(rutaArchivo, opciones);
            imageView.setImageBitmap(enderezar(bitmap));
        } else {
            imageView.setImageResource(imagenRes);
        }
    }

    // endereza la foto si salio de lado
    private android.graphics.Bitmap enderezar(android.graphics.Bitmap bitmap) {
        if (bitmap == null) return null;
        int grados = 0;
        try {
            android.media.ExifInterface exif = new android.media.ExifInterface(rutaArchivo);
            int orientacion = exif.getAttributeInt(android.media.ExifInterface.TAG_ORIENTATION,
                    android.media.ExifInterface.ORIENTATION_NORMAL);
            if (orientacion == android.media.ExifInterface.ORIENTATION_ROTATE_90) grados = 90;
            else if (orientacion == android.media.ExifInterface.ORIENTATION_ROTATE_180) grados = 180;
            else if (orientacion == android.media.ExifInterface.ORIENTATION_ROTATE_270) grados = 270;
        } catch (java.io.IOException e) {
            return bitmap;
        }
        if (grados == 0) return bitmap;
        android.graphics.Matrix matriz = new android.graphics.Matrix();
        matriz.postRotate(grados);
        return android.graphics.Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matriz, true);
    }
}
