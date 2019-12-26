package com.github.application.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.github.application.main.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZhongXiaolong on 2019/12/25 15:15.
 */
public class NoteDatabase {

    private static final String TABLE = "Note";
    private static final String TABLE_PHOTO = "NotePhoto";

    public static int insert(Note note) {
        SQLiteDatabase database = DatabaseHelper.getInstance();
        ContentValues values = new ContentValues();
        values.put("title",note.getTitle());
        values.put("content",note.getContent());
        values.put("insertTime",note.getInsertTime());
        values.put("updateTime",note.getUpdateTime());
        int id = (int) database.insert(TABLE, null, values);
        note.setId(id);
        List<Note.NotePhoto> notePhotoList = note.getNotePhotoList();
        for (int i = 0; notePhotoList != null && i < notePhotoList.size(); i++) {
            values.clear();
            Note.NotePhoto notePhoto = notePhotoList.get(i);
            values.put("noteId",id);
            values.put("path",notePhoto.getPath());
            values.put("insertTime",notePhoto.getInsertTime());
            values.put("updateTime",notePhoto.getUpdateTime());
            int photoId = (int) database.insert(TABLE_PHOTO, null, values);
            notePhoto.setId(photoId);
        }
        database.close();
        return id;
    }

    public static boolean update(Note note) {
        boolean update = false;
        SQLiteDatabase database = DatabaseHelper.getInstance();
        ContentValues values = new ContentValues();
        values.put("title",note.getTitle());
        values.put("content",note.getContent());
        values.put("updateTime",note.getUpdateTime());
        update = database.update(TABLE, values, "id=?", new String[]{String.valueOf(note.getId())}) > 0;
        List<Note.NotePhoto> newPhotoList = note.getNotePhotoList();
        List<Note.NotePhoto> oldPhotoList = findPhotoByNoteId(note.getId());
        for (Note.NotePhoto newPhoto : newPhotoList) {
            boolean insert = true;
            for (Note.NotePhoto oldPhoto : oldPhotoList) {
                if (newPhoto.getPath().equals(oldPhoto.getPath())) {
                    newPhoto.setId(oldPhoto.getId());
                    newPhoto.setUpdateTime(oldPhoto.getUpdateTime());
                    newPhoto.setInsertTime(oldPhoto.getInsertTime());
                    oldPhotoList.remove(oldPhoto);
                    insert = false;
                    break;
                }
            }
            if (insert) {
                values.clear();
                values.put("noteId",note.getId());
                values.put("path",newPhoto.getPath());
                values.put("insertTime",newPhoto.getInsertTime());
                values.put("updateTime",newPhoto.getUpdateTime());
                int photoId = (int) database.insert(TABLE_PHOTO, null, values);
                newPhoto.setId(photoId);
            }
        }

        for (Note.NotePhoto notePhoto : oldPhotoList) {
            database.delete(TABLE_PHOTO, "id=?", new String[]{String.valueOf(notePhoto.getId())});
        }
        database.close();
        return update;
    }

    public static List<Note> findNoteList(){
        List<Note> notes = new ArrayList<>();
        SQLiteDatabase database = DatabaseHelper.getInstance();
        Cursor cursor = database.rawQuery("select * from Note ORDER BY insertTime DESC", null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            List<Note.NotePhoto> notePhotos = new ArrayList<>();
            Cursor photoCursor = database.rawQuery(
                    "select * from NotePhoto where noteId=? ORDER BY insertTime DESC",
                    new String[]{String.valueOf(id)});

            while (photoCursor.moveToNext()) {
                Note.NotePhoto notePhoto = new Note.NotePhoto();
                notePhoto.setId(photoCursor.getInt(photoCursor.getColumnIndex("id")));
                notePhoto.setNoteId(photoCursor.getInt(photoCursor.getColumnIndex("noteId")));
                notePhoto.setPath(photoCursor.getString(photoCursor.getColumnIndex("path")));
                notePhoto.setInsertTime(photoCursor.getLong(photoCursor.getColumnIndex("insertTime")));
                notePhoto.setUpdateTime(photoCursor.getLong(photoCursor.getColumnIndex("updateTime")));
                notePhotos.add(notePhoto);
            }
            photoCursor.close();

            Note note = new Note();
            note.setId(id);
            note.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            note.setContent(cursor.getString(cursor.getColumnIndex("content")));
            note.setInsertTime(cursor.getLong(cursor.getColumnIndex("insertTime")));
            note.setUpdateTime(cursor.getLong(cursor.getColumnIndex("updateTime")));
            note.setNotePhotoList(notePhotos);
            notes.add(note);
        }
        cursor.close();
        database.close();
        return notes;
    }

    public static Note findNoteById(int id){
        Note note = null;
        SQLiteDatabase database = DatabaseHelper.getInstance();
        Cursor cursor = database.rawQuery("select * from Note where id=? ", new String[]{String.valueOf(id)});
        if (cursor.moveToNext()) {
            note = new Note();
            note.setId(id);
            note.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            note.setContent(cursor.getString(cursor.getColumnIndex("content")));
            note.setInsertTime(cursor.getLong(cursor.getColumnIndex("insertTime")));
            note.setUpdateTime(cursor.getLong(cursor.getColumnIndex("updateTime")));
            note.setNotePhotoList(findPhotoByNoteId(id));
        }
        cursor.close();
        database.close();
        return note;
    }

    private static List<Note.NotePhoto> findPhotoByNoteId(int noteId) {
        List<Note.NotePhoto> notePhotos = new ArrayList<>();
        SQLiteDatabase database = DatabaseHelper.getInstance();
        Cursor photoCursor = database.rawQuery(
                "select * from NotePhoto where noteId=? ORDER BY insertTime DESC",
                new String[]{String.valueOf(noteId)});

        while (photoCursor.moveToNext()) {
            Note.NotePhoto notePhoto = new Note.NotePhoto();
            notePhoto.setId(photoCursor.getInt(photoCursor.getColumnIndex("id")));
            notePhoto.setNoteId(photoCursor.getInt(photoCursor.getColumnIndex("noteId")));
            notePhoto.setPath(photoCursor.getString(photoCursor.getColumnIndex("path")));
            notePhoto.setInsertTime(photoCursor.getLong(photoCursor.getColumnIndex("insertTime")));
            notePhoto.setUpdateTime(photoCursor.getLong(photoCursor.getColumnIndex("updateTime")));
            notePhotos.add(notePhoto);
        }
        photoCursor.close();
        return notePhotos;
    }

    public static boolean deleteByNoteId(int noteId) {
        SQLiteDatabase database = DatabaseHelper.getInstance();
        String[] whereArgs = {String.valueOf(noteId)};
        int delete = database.delete(TABLE, "id=?", whereArgs);
        int deletePhoto = database.delete(TABLE_PHOTO, "noteId=?", whereArgs);
        return (delete + deletePhoto) > 0;
    }
}
