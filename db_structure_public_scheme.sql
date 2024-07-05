--
-- PostgreSQL database dump
--

-- Dumped from database version 16.3 (Debian 16.3-1.pgdg120+1)
-- Dumped by pg_dump version 16.0

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: public; Type: SCHEMA; Schema: -; Owner: pg_database_owner
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO pg_database_owner;

--
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: pg_database_owner
--

COMMENT ON SCHEMA public IS 'standard public schema';


--
-- Name: noupdate(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.noupdate() RETURNS trigger
    LANGUAGE plpgsql
    AS $$BEGIN
   RAISE EXCEPTION 'update is not allowed';
   RETURN NEW;
END;$$;


ALTER FUNCTION public.noupdate() OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: offer; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.offer (
    id integer NOT NULL,
    offer_name text NOT NULL,
    discount numeric NOT NULL,
    valid_until timestamp without time zone NOT NULL,
    valid_since timestamp without time zone NOT NULL,
    CONSTRAINT discount_interval_down CHECK ((discount > (0)::numeric))
);


ALTER TABLE public.offer OWNER TO postgres;

--
-- Name: offer_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.offer ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.offer_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: offer_requirement_bridge; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.offer_requirement_bridge (
    offer_id integer NOT NULL,
    product_id integer NOT NULL,
    required_count integer NOT NULL,
    id integer NOT NULL
);


ALTER TABLE public.offer_requirement_bridge OWNER TO postgres;

--
-- Name: offer_requirement_bridge_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.offer_requirement_bridge ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.offer_requirement_bridge_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: payment_method; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.payment_method (
    id integer NOT NULL,
    method_name text NOT NULL
);


ALTER TABLE public.payment_method OWNER TO postgres;

--
-- Name: product; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.product (
    id integer NOT NULL,
    product_name text NOT NULL,
    barcode text NOT NULL,
    current_price numeric NOT NULL
);


ALTER TABLE public.product OWNER TO postgres;

--
-- Name: product_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.product ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.product_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: role_user_bridge; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.role_user_bridge (
    role_id integer NOT NULL,
    user_id integer NOT NULL
);


ALTER TABLE public.role_user_bridge OWNER TO postgres;

--
-- Name: sale; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sale (
    id integer NOT NULL,
    received_money numeric NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    cashier integer NOT NULL,
    payment_method integer DEFAULT 1 NOT NULL
);


ALTER TABLE public.sale OWNER TO postgres;

--
-- Name: sale_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.sale ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.sale_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: sale_offer; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sale_offer (
    sale_id integer NOT NULL,
    offer_id integer NOT NULL
);


ALTER TABLE public.sale_offer OWNER TO postgres;

--
-- Name: sale_product_bridge; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sale_product_bridge (
    sale_id integer,
    product_id integer NOT NULL,
    product_count integer NOT NULL,
    unit_price integer NOT NULL,
    id integer NOT NULL
);


ALTER TABLE public.sale_product_bridge OWNER TO postgres;

--
-- Name: sale_product_bridge_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.sale_product_bridge ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.sale_product_bridge_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: user; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public."user" (
    id integer NOT NULL,
    username text NOT NULL,
    pw character varying(72) NOT NULL,
    deleted boolean DEFAULT false NOT NULL,
    full_name text NOT NULL
);


ALTER TABLE public."user" OWNER TO postgres;

--
-- Name: table1_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public."user" ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.table1_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: user_role; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_role (
    id integer NOT NULL,
    role_name text NOT NULL
);


ALTER TABLE public.user_role OWNER TO postgres;

--
-- Name: user_role_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.user_role ALTER COLUMN id ADD GENERATED ALWAYS AS IDENTITY (
    SEQUENCE NAME public.user_role_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- Name: offer offer_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.offer
    ADD CONSTRAINT offer_pkey PRIMARY KEY (id);


--
-- Name: offer_requirement_bridge offer_requirement_bridge_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.offer_requirement_bridge
    ADD CONSTRAINT offer_requirement_bridge_pkey PRIMARY KEY (id);


--
-- Name: product product_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT product_pkey PRIMARY KEY (id);


--
-- Name: role_user_bridge role_user_bridge_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role_user_bridge
    ADD CONSTRAINT role_user_bridge_pkey PRIMARY KEY (role_id, user_id);


--
-- Name: sale sale_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sale
    ADD CONSTRAINT sale_pkey PRIMARY KEY (id);


--
-- Name: sale_product_bridge table1_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sale_product_bridge
    ADD CONSTRAINT table1_pkey PRIMARY KEY (id);


--
-- Name: payment_method unique_payment_method_id; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.payment_method
    ADD CONSTRAINT unique_payment_method_id PRIMARY KEY (id);


--
-- Name: product unique_product_barcode; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.product
    ADD CONSTRAINT unique_product_barcode UNIQUE (barcode);


--
-- Name: user unique_table1_username; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."user"
    ADD CONSTRAINT unique_table1_username UNIQUE (username);


--
-- Name: user user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public."user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);


--
-- Name: user_role user_role_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_role
    ADD CONSTRAINT user_role_pkey PRIMARY KEY (id);


--
-- Name: offer_requirement_bridge_offer_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX offer_requirement_bridge_offer_id_idx ON public.offer_requirement_bridge USING btree (offer_id);


--
-- Name: received_money_index; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX received_money_index ON public.sale USING btree (received_money);


--
-- Name: role_user_bridge_role_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX role_user_bridge_role_id_idx ON public.role_user_bridge USING btree (role_id);


--
-- Name: role_user_bridge_user_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX role_user_bridge_user_id_idx ON public.role_user_bridge USING btree (user_id);


--
-- Name: sale_product_bridge_sale_id_idx; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX sale_product_bridge_sale_id_idx ON public.sale_product_bridge USING btree (sale_id);


--
-- Name: offer noupdate; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER noupdate BEFORE UPDATE ON public.offer FOR EACH ROW EXECUTE FUNCTION public.noupdate();


--
-- Name: offer_requirement_bridge noupdate; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER noupdate BEFORE UPDATE ON public.offer_requirement_bridge FOR EACH ROW EXECUTE FUNCTION public.noupdate();


--
-- Name: product noupdate; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER noupdate BEFORE UPDATE ON public.product FOR EACH ROW EXECUTE FUNCTION public.noupdate();


--
-- Name: sale_offer noupdate; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER noupdate BEFORE UPDATE ON public.sale_offer FOR EACH ROW EXECUTE FUNCTION public.noupdate();


--
-- Name: sale_product_bridge noupdate; Type: TRIGGER; Schema: public; Owner: postgres
--

CREATE TRIGGER noupdate BEFORE UPDATE ON public.sale_product_bridge FOR EACH ROW EXECUTE FUNCTION public.noupdate();


--
-- Name: offer_requirement_bridge lnk_offer_MM_offer_requirement_bridge; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.offer_requirement_bridge
    ADD CONSTRAINT "lnk_offer_MM_offer_requirement_bridge" FOREIGN KEY (offer_id) REFERENCES public.offer(id) MATCH FULL ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: sale_offer lnk_offer_MM_sale; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sale_offer
    ADD CONSTRAINT "lnk_offer_MM_sale" FOREIGN KEY (offer_id) REFERENCES public.offer(id) MATCH FULL ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: offer_requirement_bridge lnk_product_MM_offer_requirement_bridge; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.offer_requirement_bridge
    ADD CONSTRAINT "lnk_product_MM_offer_requirement_bridge" FOREIGN KEY (product_id) REFERENCES public.product(id) MATCH FULL;


--
-- Name: sale_product_bridge lnk_product_MM_sale_product_bridge; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sale_product_bridge
    ADD CONSTRAINT "lnk_product_MM_sale_product_bridge" FOREIGN KEY (product_id) REFERENCES public.product(id) MATCH FULL;


--
-- Name: sale_offer lnk_sale_MM_offer; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sale_offer
    ADD CONSTRAINT "lnk_sale_MM_offer" FOREIGN KEY (sale_id) REFERENCES public.sale(id) MATCH FULL ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: sale_product_bridge lnk_sale_MM_sale_product_bridge; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sale_product_bridge
    ADD CONSTRAINT "lnk_sale_MM_sale_product_bridge" FOREIGN KEY (sale_id) REFERENCES public.sale(id) MATCH FULL;


--
-- Name: sale lnk_sale_OM_payment_method; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sale
    ADD CONSTRAINT "lnk_sale_OM_payment_method" FOREIGN KEY (payment_method) REFERENCES public.payment_method(id) MATCH FULL;


--
-- Name: role_user_bridge lnk_user_MM_user_role; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role_user_bridge
    ADD CONSTRAINT "lnk_user_MM_user_role" FOREIGN KEY (user_id) REFERENCES public."user"(id) MATCH FULL ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: sale lnk_user_OM_sale; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sale
    ADD CONSTRAINT "lnk_user_OM_sale" FOREIGN KEY (cashier) REFERENCES public."user"(id) MATCH FULL;


--
-- Name: role_user_bridge lnk_user_role_MM_user; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role_user_bridge
    ADD CONSTRAINT "lnk_user_role_MM_user" FOREIGN KEY (role_id) REFERENCES public.user_role(id) MATCH FULL ON UPDATE CASCADE ON DELETE CASCADE;


--
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: pg_database_owner
--

REVOKE USAGE ON SCHEMA public FROM PUBLIC;


--
-- PostgreSQL database dump complete
--

