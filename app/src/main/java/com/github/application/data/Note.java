package com.github.application.data;

import java.util.List;

/**
 * Created by ZhongXiaolong on 2019/12/25 14:59.
 * id INTEGER PRIMARY KEY AUTOINCREMENT, title VARCHAR(200),content VARCHAR(200),insertTime BIGINT,updateTime BIGINT,
 * deleteFlag INTEGER DEFAULT (0)
 * id INTEGER PRIMARY KEY AUTOINCREMENT, path VARCHAR(200),insertTime BIGINT,updateTime BIGINT,deleteFlag INTEGER
 * DEFAULT (0)
 */
public class Note {
    private int id;
    private String title;
    private String content;
    private long insertTime;
    private long updateTime;
    private int deleteFlag;
    private List<NotePhoto> notePhotoList;

    public int getId() {
        return id;
    }

    public Note setId(int id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Note setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Note setContent(String content) {
        this.content = content;
        return this;
    }

    public long getInsertTime() {
        return insertTime;
    }

    public Note setInsertTime(long insertTime) {
        this.insertTime = insertTime;
        return this;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public Note setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public int getDeleteFlag() {
        return deleteFlag;
    }

    public Note setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
        return this;
    }

    public List<NotePhoto> getNotePhotoList() {
        return notePhotoList;
    }

    public Note setNotePhotoList(List<NotePhoto> notePhotoList) {
        this.notePhotoList = notePhotoList;
        return this;
    }

    public static class NotePhoto{
        private int id;
        private int noteId;
        private String path;
        private long insertTime;
        private long updateTime;
        private int deleteFlag;

        public int getId() {
            return id;
        }

        public NotePhoto setId(int id) {
            this.id = id;
            return this;
        }

        public int getNoteId() {
            return noteId;
        }

        public NotePhoto setNoteId(int noteId) {
            this.noteId = noteId;
            return this;
        }

        public String getPath() {
            return path;
        }

        public NotePhoto setPath(String path) {
            this.path = path;
            return this;
        }

        public long getInsertTime() {
            return insertTime;
        }

        public NotePhoto setInsertTime(long insertTime) {
            this.insertTime = insertTime;
            return this;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public NotePhoto setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
            return this;
        }

        public int getDeleteFlag() {
            return deleteFlag;
        }

        public NotePhoto setDeleteFlag(int deleteFlag) {
            this.deleteFlag = deleteFlag;
            return this;
        }
    }
}
