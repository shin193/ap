# وب‌سرویس کتابخانه با Spring Boot

این بخش یک REST API با Spring Boot 3.5، Spring Security، اعتبارسنجی Jakarta و SQLite است.
کد کنسولی در محل قبلی باقی مانده و ماژول `core` همان فایل‌ها را کامپایل می‌کند.

## اجرا در IntelliJ

برای پروژهٔ فعلی یک تنظیم اجرای آماده به نام **Library Web Service** اضافه شده است.
این تنظیم از پورت `8090` استفاده می‌کند: `http://127.0.0.1:8090/api/health`.
فایل `requests.http` نیز برای همین پورت تنظیم شده است.
پس از ساخت، آن را از فهرست Run انتخاب و اجرا کنید. ساخت مجدد با Maven داخلی IntelliJ نیز ممکن است:

```powershell
.\Final_project\build-webservice.ps1
```

برای ویرایش و اجرای مستقیم سورس Spring Boot:

1. فایل `Final_project/pom.xml` را به‌عنوان پروژهٔ Maven باز کنید؛ JDK 17 یا جدیدتر انتخاب شود.
2. پس از دریافت وابستگی‌ها، کلاس `Final_project.web.LibraryWebApplication` را اجرا کنید.
3. برای استفاده از دیتابیس قبلی، Working directory را روی ریشهٔ ریپازیتوری `ap` بگذارید،
   یا متغیر محیطی `LIBRARY_DB_PATH` را برابر مسیر کامل فایل `library.db` تنظیم کنید.
4. نشانی بررسی اجرا: `http://127.0.0.1:8080/api/health`.

فایل `requests.http` شامل درخواست‌های آماده برای HTTP Client در IntelliJ یا انتقال به Postman است.
حالت کنسولی همچنان با کلاس `Final_project.LibrarySystem` قابل اجراست.

## ساخت از خط فرمان

از ریشهٔ ریپازیتوری، با Maven 3.6.3 یا جدیدتر:

```powershell
mvn -f Final_project/pom.xml clean verify
java -jar Final_project/webservice/target/library-webservice-1.0.0.jar
```

Maven Wrapper هم در پروژه قرار دارد؛ در صورت نصب‌نبودن Maven، دستور معادل در ویندوز:

```powershell
.\Final_project\mvnw.cmd -f Final_project/pom.xml clean verify
```

پورت پیش‌فرض ۸۰۸۰ است؛ برای تغییر:

```powershell
java -jar Final_project/webservice/target/library-webservice-1.0.0.jar --server.port=8090
```

از نسخهٔ کنسولی و وب‌سرویس به‌صورت جداگانه استفاده کنید؛ نسخهٔ کنسولی لیست داده‌ها را
در حافظه نگه می‌دارد و ذخیرهٔ هم‌زمان آن می‌تواند تغییرات وب‌سرویس را بازنویسی کند.
در خود API، درخواست‌های هم‌زمان از SQL شرطی استفاده می‌کنند تا یک کتاب دوبار امانت داده نشود.
فایل‌های قدیمی `.dat` از پوشهٔ کنار دیتابیس خوانده می‌شوند و تست‌ها از دیتابیس موقت استفاده می‌کنند.

## ورود و دسترسی

```http
POST /api/auth/login
Content-Type: application/json

{"role":"LIBRARIAN","username":"abc","password":"123456"}
```

نقش‌ها: `STUDENT`، `LIBRARIAN` و `MANAGER`.
حساب‌های قبلی و حساب‌های اولیهٔ مدیر (`ADMIN` / `1111`) و کتابدار (`abc` / `123456`) قابل استفاده‌اند.
خروجی ورود شامل `token`، `tokenType`، `expiresAt` و اطلاعات کاربر بدون رمز است.
در درخواست‌های بعدی هدر زیر را بفرستید:

```http
Authorization: Bearer TOKEN
```

توکن‌ها پس از ۶۰ دقیقه، خروج کاربر یا راه‌اندازی مجدد سرویس نامعتبر می‌شوند.
غیرفعال‌کردن حساب دانشجو، دسترسی توکن‌های فعلی او را هم قطع می‌کند.
رمز حساب‌های جدید با PBKDF2 و salt تصادفی ذخیره می‌شود. رمزهای قدیمی هنگام ورود موفق
به API ارتقا می‌یابند؛ ورود در نسخهٔ کنسولی هر دو قالب را پشتیبانی می‌کند.
سرویس پیش‌فرض فقط روی همان کامپیوتر (`127.0.0.1`) در دسترس است.

## مسیرها

| روش | مسیر | دسترسی و کاربرد |
|---|---|---|
| GET | `/api/health` | عمومی؛ وضعیت سرویس |
| POST | `/api/students` | عمومی؛ ثبت‌نام با name، studentId، username، password |
| POST | `/api/auth/login` | عمومی؛ ورود و دریافت توکن |
| GET | `/api/auth/me` | کاربر واردشده؛ مشخصات خودش |
| POST | `/api/auth/logout` | کاربر واردشده؛ ابطال توکن |
| GET | `/api/books` | عمومی؛ فیلترهای اختیاری title، author، year، available |
| GET | `/api/books/{id}` | عمومی؛ مشخصات کتاب بدون اطلاعات شخصی امانت‌گیرنده |
| POST | `/api/books` | کتابدار/مدیر؛ افزودن id، title، author، publishYear |
| PUT | `/api/books/{id}` | کتابدار/مدیر؛ ویرایش title، author، publishYear |
| DELETE | `/api/books/{id}` | کتابدار/مدیر؛ حذف کتاب موجود بدون درخواست فعال |
| POST | `/api/books/{id}/borrow-requests` | دانشجو؛ درخواست با بدنهٔ `{"days":7}`، بین ۱ تا ۳۶۵ روز |
| POST | `/api/books/{id}/approve` | کتابدار/مدیر؛ تأیید درخواست |
| POST | `/api/books/{id}/receive` | کتابدار/مدیر؛ ثبت تحویل کتاب تأییدشده |
| POST | `/api/books/{id}/return` | دانشجو برای امانت خودش؛ کتابدار/مدیر برای هر امانت |
| GET | `/api/loans` | دانشجو: درخواست‌ها و امانت‌های خودش؛ کتابدار/مدیر: همهٔ موارد فعال |
| GET | `/api/students` | کتابدار/مدیر؛ فهرست دانشجوها بدون رمز |
| PATCH | `/api/students/{id}/active` | کتابدار/مدیر؛ بدنهٔ `{"active":false}` |
| POST | `/api/librarians` | مدیر؛ افزودن username، password، name |
| GET | `/api/statistics` | کتابدار/مدیر؛ شمارش کتاب‌ها، دانشجوها و امانت‌های فعال |

این API وضعیت امانت فعلی را ارائه می‌کند؛ اسکیمای فعلی پروژه تاریخچهٔ مستقل امانت‌ها ندارد.

پاسخ‌های معمول: `200` موفق، `201` ایجاد، `204` موفق بدون بدنه، `400` ورودی نامعتبر،
`401` نیاز به ورود، `403` دسترسی غیرمجاز، `404` پیدا نشد، `409` تکرار/تعارض وضعیت و
`503` دیتابیس موقتاً در دسترس نیست. خطاها JSON شامل `code` و `message` هستند.

## تست‌ها

```powershell
mvn -f Final_project/pom.xml verify
python Final_project/tests/run_tests.py
```

تست‌های Spring Boot سرور واقعی روی پورت تصادفی و SQLite موقت ایجاد می‌کنند و ورود،
دسترسی نقش‌ها، CRUD، اعتبارسنجی، مالکیت امانت، غیرفعال‌سازی و درخواست هم‌زمان را بررسی می‌کنند.
