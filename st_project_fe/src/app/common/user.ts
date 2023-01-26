export class User {
    id: string | undefined;
    realname: string | undefined;
    username: string | undefined;
    password: string | undefined;
    role: string | undefined;
    tenant: Tenant | undefined;
}
export class Tenant {
    id: string | undefined;
    tenantValue: string | undefined;

}
